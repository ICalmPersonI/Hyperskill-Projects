package account.user;

import account.group.Group;
import account.user.dto.ChangeAccessRequest;
import account.util.Utils;
import account.user.dto.ChangePasswordRequest;
import account.user.dto.ChangeRoleRequest;
import account.user.dto.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Locale;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    //Authentication
    @PostMapping("/api/auth/signup")
    ResponseEntity<?> singUp(@Valid @RequestBody CreateUserRequest request, Errors errors) {
        if (errors.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(service.register(request.getName(), request.getLastname(),
                request.getEmail().toLowerCase(Locale.ROOT), request.getPassword()),
                Utils.JSON_HEADERS,
                HttpStatus.OK);
    }

    @PostMapping("/api/auth/changepass")
    ResponseEntity<?> changePass(@Valid @RequestBody ChangePasswordRequest request, Errors errors) {
        if (errors.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(
                service.changePass(Utils.getCurrentUser().getUser().getId(), request.getPassword()),
                Utils.JSON_HEADERS,
                HttpStatus.OK);
    }

    //Service
    @PutMapping("/api/admin/user/role")
    ResponseEntity<?> changeRole(@RequestBody ChangeRoleRequest request) {
        return new ResponseEntity<>(
                service.changeRole(request.getUser().toLowerCase(Locale.ROOT),
                        Group.PREFIX_ROLE + request.getRole(),
                        request.getOperation()),
                Utils.JSON_HEADERS,
                HttpStatus.OK);
    }

    @DeleteMapping("/api/admin/user/{user email}")
    ResponseEntity<?> deleteUser(@PathVariable("user email") String email) {
        return new ResponseEntity<>(service.delete(email), Utils.JSON_HEADERS, HttpStatus.OK);
    }

    @GetMapping("/api/admin/user")
    ResponseEntity<?> showInformation() {
        return new ResponseEntity<>(service.getAllUsers(), Utils.JSON_HEADERS, HttpStatus.OK);
    }

    @PutMapping("/api/admin/user/access")
    ResponseEntity<?> access(@Valid @RequestBody ChangeAccessRequest request) {
        return new ResponseEntity<>(
                service.changeAccess(request.getUser().toLowerCase(Locale.ROOT), request.getOperation()),
                Utils.JSON_HEADERS,
                HttpStatus.OK);
    }
}
