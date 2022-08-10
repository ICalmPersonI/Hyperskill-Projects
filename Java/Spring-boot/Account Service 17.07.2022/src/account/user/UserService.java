package account.user;

import account.event.Event;
import account.event.EventService;
import account.user.dto.*;
import account.group.Group;
import account.group.GroupRepository;
import account.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.StreamSupport;


@Service
public class UserService implements UserDetailsService {

    public static final int MAX_FAILED_ATTEMPTS = 5;

    private static final List<String> BAD_PASSWORDS = List.of(
            "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder encoder;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmailIgnoreCase(username.toLowerCase(Locale.ROOT))
                .map(user -> new UserDetailsImpl(user, getAuthorities(user)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    public Optional<User> getByEmail(String email) {
       return userRepository.findUserByEmailIgnoreCase(email);
    }

    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
    }

    public void resetFailedAttempts(String email) {
        userRepository.updateFailedAttempts(0, email);
    }

    UserInfo register(String name, String lastname, String email, String password) {
        if (userRepository.findUserByEmailIgnoreCase(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }
        if (BAD_PASSWORDS.contains(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
        User newUser = new User(name, lastname, email, encoder.encode(password));
        grantRole(newUser, userRepository.existsById(1L) ?
                Group.PREFIX_ROLE + Group.USER : Group.PREFIX_ROLE + Group.ADMINISTRATOR);
        User savedUser = userRepository.save(newUser);
        eventService.save(Event.CREATE_USER, "Anonymous", email, "/api/auth/signup");
        return new UserInfo(savedUser.getId(),
                savedUser.getName(),
                savedUser.getLastname(),
                savedUser.getEmail().toLowerCase(Locale.ROOT),
                savedUser.getUserGroups().stream().map(Group::getCode).sorted().toArray(String[]::new)
        );
    }

    ChangePasswordResponse changePass(long id, String newPassword) {
        return userRepository.findById(id).map(user -> {
            if (BAD_PASSWORDS.contains(newPassword)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
            }
            if (encoder.matches(newPassword, user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
            }
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            eventService.save(
                    Event.CHANGE_PASSWORD,
                    user.getEmail(),
                    Utils.getCurrentUser().getUser().getEmail(),
                    "/api/auth/changepass");
            return new ChangePasswordResponse(user.getEmail(), "The password has been updated successfully");
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    UserInfo[] getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparingLong(User::getId))
                .map(user -> new UserInfo(
                        user.getId(),
                        user.getName(),
                        user.getLastname(),
                        user.getEmail(),
                        user.getUserGroups().stream().map(Group::getCode).sorted().toArray(String[]::new)
                )).toArray(UserInfo[]::new);
    }

    Feedback delete(String email) {
        return userRepository.findUserByEmailIgnoreCase(email).map(user -> {
            if (user.getUserGroups().stream().anyMatch(group -> group.getCode()
                    .equals(Group.PREFIX_ROLE + Group.ADMINISTRATOR))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
            }
            userRepository.delete(user);
            eventService.save(
                    Event.DELETE_USER,
                    Utils.getCurrentUser().getUser().getEmail(),
                    user.getEmail(),
                    "/api/admin/user");
            return new Feedback(user.getEmail(), "Deleted successfully!");
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    @Transactional
    public ChangeAccessResponse changeAccess(String email, String operation) {
        return userRepository.findUserByEmailIgnoreCase(email).map(user -> {
            switch (operation) {
                case "LOCK": {
                    lock(user);
                    break;
                }
                case "UNLOCK": {
                    unlock(user);
                    break;
                }
                default: throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong operation!");
            }
            userRepository.save(user);
            return new ChangeAccessResponse(String.format("User %s %sed!", email, operation.toLowerCase(Locale.ROOT)));
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    @Transactional
    public void autoLockUser(String email, String endpoint) {
        Optional<User> user = userRepository.findUserByEmailIgnoreCase(email);
        if (user.isPresent()) {
            if (user.get().getUserGroups().stream()
                    .anyMatch(group -> group.getCode().equals(Group.PREFIX_ROLE + Group.ADMINISTRATOR))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't lock the ADMINISTRATOR!");
            }
            user.get().setNonLocked(false);
            userRepository.save(user.get());
            eventService.save(Event.LOCK_USER, email, String.format("Lock user %s", email), endpoint);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
    }

    @Transactional
    private void lock(User user) {
        if (user.getUserGroups().stream().anyMatch(group -> group.getCode().equals("ROLE_ADMINISTRATOR"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't lock the ADMINISTRATOR!");
        }
        user.setNonLocked(false);

        eventService.save(
                Event.LOCK_USER,
                Utils.getCurrentUser().getUser().getEmail().toLowerCase(Locale.ROOT),
                String.format("Lock user %s", user.getEmail()),
                "/api/admin/user/access");
    }

    private void unlock(User user) {
        user.setNonLocked(true);
        user.setFailedAttempt(0);

        eventService.save(
                Event.UNLOCK_USER,
                Utils.getCurrentUser().getUser().getEmail().toLowerCase(Locale.ROOT),
                String.format("Unlock user %s", user.getEmail()),
                "/api/admin/user/access");
    }


    UserInfo changeRole(String email, String role, String operation) {
        return userRepository.findUserByEmailIgnoreCase(email).map(user -> {
            switch (operation) {
                case "GRANT": {
                    grantRole(user, role);
                    eventService.save(Event.GRANT_ROLE,
                            Utils.getCurrentUser().getUser().getEmail(),
                            String.format("Grant role %s to %s",
                                    role.replace(Group.PREFIX_ROLE, ""), email),
                            "/api/admin/user/role");
                    break;
                }
                case "REMOVE": {
                    removeRole(user, role);
                    eventService.save(Event.REMOVE_ROLE,
                            Utils.getCurrentUser().getUser().getEmail(),
                            String.format("Remove role %s from %s",
                                    role.replace(Group.PREFIX_ROLE, ""), email),
                            "/api/admin/user/role");
                    break;
                }
                default: throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong operation!");
            }
            userRepository.save(user);

            return new UserInfo(user.getId(),
                    user.getName(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getUserGroups().stream().map(Group::getCode).sorted().toArray(String[]::new));

        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    private void removeRole(User user, String role) {
        if (user.getUserGroups().stream().anyMatch(group -> group.getCode().equals(role))) {
            if (role.equals(Group.PREFIX_ROLE + Group.ADMINISTRATOR)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
            }
            if (user.getUserGroups().size() <= 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
            }
            Set<Group> groups = user.getUserGroups();
            groups.removeIf(group -> group.getCode().equals(role));
            user.setUserGroups(user.getUserGroups());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
        }
    }

    private void grantRole(User user, String role) {
        Optional<Group> group = groupRepository.findByCode(role);
        if (group.isPresent()) {
            Set<Group> groups = user.getUserGroups();
            if ((groups.stream().anyMatch(g -> g.getName().equals("Admin group")) &&
                    Group.CUSTOMER_GROUP.stream().anyMatch(g -> g.getCode().equals(role) )) ||
                    (groups.stream().anyMatch(g -> g.getName().equals("Customer group")) &&
                            Group.ADMIN_GROUP.stream().anyMatch(g -> g.getCode().equals(role)))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "The user cannot combine administrative and business roles!");
            }
            user.addUserGroup(group.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }
    }

    private Collection<GrantedAuthority> getAuthorities(User user) {
        Set<Group> groups = user.getUserGroups();
        Collection<GrantedAuthority> authorities = new ArrayList<>(groups.size());
        groups.stream().map(group -> new SimpleGrantedAuthority(group.getCode())).forEach(authorities::add);
        return authorities;
    }
}
