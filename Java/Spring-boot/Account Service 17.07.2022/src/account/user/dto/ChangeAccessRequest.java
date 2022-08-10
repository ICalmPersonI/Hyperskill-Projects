package account.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class ChangeAccessRequest {

    @NotEmpty
    private String user;

    @NotEmpty
    private String operation;

    @JsonCreator
    public ChangeAccessRequest(@JsonProperty("user") String user,
                               @JsonProperty("operation") String operation) {
        this.user = user;
        this.operation = operation;
    }

    public String getUser() {
        return user;
    }

    public String getOperation() {
        return operation;
    }
}
