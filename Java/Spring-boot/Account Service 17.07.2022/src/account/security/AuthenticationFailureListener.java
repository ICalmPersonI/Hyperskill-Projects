package account.security;

import account.event.Event;
import account.event.EventService;
import account.group.Group;
import account.user.User;
import account.user.UserService;
import account.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;


    @Override
    @Transactional
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String email = Utils.getUsernameFormAuthorizationHeader(request);
        Optional<User> user = userService.getByEmail(email.toLowerCase(Locale.ROOT));
        if (user.isPresent() &&
                user.get().getUserGroups().stream()
                        .noneMatch(group -> group.getCode().equals(Group.PREFIX_ROLE + Group.ADMINISTRATOR))) {
            if (!user.get().isNonLocked()) {
                eventLoginFailed(email);
            } else {
                int failedAttempts = user.get().getFailedAttempt();
                if (failedAttempts < UserService.MAX_FAILED_ATTEMPTS) {
                    userService.increaseFailedAttempts(user.get());
                    failedAttempts++;
                    eventLoginFailed(email);
                }
                if (failedAttempts >= UserService.MAX_FAILED_ATTEMPTS) {
                    eventService.save(Event.BRUTE_FORCE, email, request.getRequestURI(), request.getRequestURI());
                    userService.autoLockUser(email, request.getRequestURI());
                }
            }
        } else {
            eventLoginFailed(email);
        }
    }

    private void eventLoginFailed(String email) {
        eventService.save(Event.LOGIN_FAILED, email, request.getRequestURI(), request.getRequestURI());
    }
}
