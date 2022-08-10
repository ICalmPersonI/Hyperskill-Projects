package account.util;


import account.user.UserDetailsImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Utils {
    public static final HttpHeaders JSON_HEADERS;

    static {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSON_HEADERS = headers;
    }

    public static UserDetailsImpl getCurrentUser() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getUsernameFormAuthorizationHeader(HttpServletRequest request) {
        String authorize = request.getHeader(HttpHeaders.AUTHORIZATION);
        return new String(
                Base64.getDecoder().decode(authorize.substring(6)), StandardCharsets.UTF_8
        ).split(":")[0];
    }
}
