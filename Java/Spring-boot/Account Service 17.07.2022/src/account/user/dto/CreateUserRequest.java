package account.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Locale;

public class CreateUserRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String lastname;

    @NotEmpty
    @Pattern(regexp = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@acme\\.com)$")
    private String email;

    @NotEmpty
    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    private String password;

    @JsonCreator
    public CreateUserRequest (@JsonProperty("name") String name,
         @JsonProperty("lastname") String lastname,
         @JsonProperty("email") String email,
         @JsonProperty("password") String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
