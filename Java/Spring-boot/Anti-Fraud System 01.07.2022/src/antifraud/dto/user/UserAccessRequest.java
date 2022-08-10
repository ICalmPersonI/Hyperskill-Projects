package antifraud.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserAccessRequest {

    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    @NotNull
    private String operation;

    @JsonCreator
    public UserAccessRequest(@JsonProperty("username") String username,
                             @JsonProperty("operation") String operation) {
        this.username = username;
        this.operation = operation;
    }

    public String getUsername() {
        return username;
    }

    public String getOperation() {
        return operation;
    }
}
