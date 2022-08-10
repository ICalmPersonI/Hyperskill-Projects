package antifraud.user;

import antifraud.Util;
import antifraud.dto.user.DeletedUserStatusResponse;
import antifraud.dto.user.UserAccessRequest;
import antifraud.dto.user.UserRoleRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private UserDetailsServiceImpl service;

    UserController(UserDetailsServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/user")
    ResponseEntity<?> auth(@Valid @RequestBody User user) {
        return service.save(user).map(value -> new ResponseEntity<>(value, Util.JSON_HEADERS, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(new HttpHeaders(), HttpStatus.CONFLICT));
    }

    @GetMapping("/list")
    ResponseEntity<?> listOfUsers() {
        return new ResponseEntity<>(service.getAll(), Util.JSON_HEADERS, HttpStatus.OK);
    }

    @DeleteMapping("/user/{username}")
    ResponseEntity<?> delete(@PathVariable("username") String username) {
        service.delete(username);
        return new ResponseEntity<>(new DeletedUserStatusResponse(username, "Deleted successfully!"),
                Util.JSON_HEADERS, HttpStatus.OK);
    }

    @PutMapping("/role")
    ResponseEntity<?> changeRole(@Valid @RequestBody UserRoleRequest request) {
        return new ResponseEntity<>(service.changeRole(request), Util.JSON_HEADERS, HttpStatus.OK);
    }

    @PutMapping("/access")
    ResponseEntity<?> changeAccess(@Valid @RequestBody UserAccessRequest request) {
        return new ResponseEntity<>(service.changeAccess(request), Util.JSON_HEADERS, HttpStatus.OK);
    }
}
