package cinema.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
    @JsonProperty("error")
    private String message;

    Error() {

    }

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
