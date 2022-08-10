package account.security;

import account.event.Event;
import account.event.EventService;
import account.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Autowired
    private EventService eventService;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String email = Utils.getUsernameFormAuthorizationHeader(request);
        eventService.save(Event.ACCESS_DENIED, email, request.getRequestURI(), request.getRequestURI());

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("timestamp", Instant.now().toString());
        objectNode.put("status", HttpStatus.FORBIDDEN.value());
        objectNode.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        objectNode.put("message","Access Denied!");
        objectNode.put("path", request.getRequestURI());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(objectNode.toString());
    }
}
