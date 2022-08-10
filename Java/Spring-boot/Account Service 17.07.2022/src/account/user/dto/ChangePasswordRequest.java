package account.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ChangePasswordRequest {

    @NotEmpty
    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    private String password;

    @JsonCreator
    public ChangePasswordRequest(@JsonProperty("new_password") String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
