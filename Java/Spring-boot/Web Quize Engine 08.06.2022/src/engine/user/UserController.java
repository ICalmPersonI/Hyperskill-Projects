package engine.user;

import com.sun.net.httpserver.Headers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    private UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("api/register")
    ResponseEntity<?> register(@Valid @RequestBody User user) {
        return service.save(user) ? new ResponseEntity<>(new Headers(), HttpStatus.OK) :
                new ResponseEntity<>(new Headers(), HttpStatus.BAD_REQUEST);
    }
}
