package antifraud.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserRoleRequest {
    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    @NotNull
    private String role;

    @JsonCreator
    public UserRoleRequest(@JsonProperty("username") String username,
                           @JsonProperty("role") String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
