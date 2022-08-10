package account.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class ChangeRoleRequest {
    @NotEmpty
    private String user;

    @NotEmpty
    private String role;

    @NotEmpty
    private String operation;

    public ChangeRoleRequest(@JsonProperty("user") String user,
                   @JsonProperty("role") String role,
                   @JsonProperty("operation") String operation) {
        this.user = user;
        this.role = role;
        this.operation = operation;
    }

    public String getUser() {
        return user;
    }

    public String getRole() {
        return role;
    }

    public String getOperation() {
        return operation;
    }
}
