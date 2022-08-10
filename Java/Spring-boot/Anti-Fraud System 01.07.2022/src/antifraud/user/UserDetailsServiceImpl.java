package antifraud.user;

import antifraud.dto.user.UserAccessRequest;
import antifraud.dto.user.UserAccessResponse;
import antifraud.dto.user.UserRoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository repository;

    BCryptPasswordEncoder encoder;

    @Autowired
    UserDetailsServiceImpl(UserRepository repository, @Lazy BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsernameIgnoreCase(username).map(UserDetailsImpl::new).orElseThrow(() -> new UsernameNotFoundException(""));
    }

    Optional<User> save(User user) {
        if (!repository.existsByUsernameIgnoreCase(user.getUsername())) {
            user.setPassword(encoder.encode(user.getPassword()));
            if (repository.findAll().spliterator().getExactSizeIfKnown() == 0) {
                user.setRole("ADMINISTRATOR");
                user.setLocked(false);
            } else {
                user.setRole("MERCHANT");
            }
            return Optional.of(repository.save(user));
        }
        return Optional.empty();
    }

    User changeRole(UserRoleRequest request) {
        if (!(request.getRole().equals("SUPPORT") || request.getRole().equals("MERCHANT"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return repository.findByUsernameIgnoreCase(request.getUsername()).map(user -> {
            if (user.getRole().equals(request.getRole())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
            user.setRole(request.getRole());
            repository.save(user);
            return user;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    UserAccessResponse changeAccess(UserAccessRequest request) {
        return repository.findByUsernameIgnoreCase(request.getUsername()).map(user -> {
            if (user.getRole().equals("ADMINISTRATOR")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            UserAccessResponse response;
            switch (request.getOperation()) {
                case "LOCK": {
                    user.setLocked(true);
                    response = new UserAccessResponse(user.getUsername(), "locked");
                    break;
                }
                case "UNLOCK": {
                    user.setLocked(false);
                    response = new UserAccessResponse(user.getUsername(), "unlocked");
                    break;
                }
                default: throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            repository.save(user);
            return response;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    Optional<User> get(String username) {
        return repository.findByUsernameIgnoreCase(username);
    }

    void delete(String username) {
        Optional<User> user = repository.findByUsernameIgnoreCase(username);
        if (user.isPresent()) {
            repository.delete(user.get());
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    List<User> getAll() {
        return repository.findAllByOrderByIdAsc();
    }
}
