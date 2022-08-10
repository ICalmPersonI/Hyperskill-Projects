package account.security;

import account.user.User;
import account.user.UserService;
import account.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent event) {
        String email = Utils.getUsernameFormAuthorizationHeader(request);
        Optional<User> user = userService.getByEmail(email.toLowerCase(Locale.ROOT));
        if (user.isPresent()) {
            if (user.get().getFailedAttempt() > 0) {
                userService.resetFailedAttempts(user.get().getEmail());
            }
        }
    }
}