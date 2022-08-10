package antifraud.dto.user;

public class UserAccessResponse {

    private String status;

    public UserAccessResponse(String username, String status) {
        this.status = String.format("User %s %s!", username, status);
    }

    public String getStatus() {
        return status;
    }
}
