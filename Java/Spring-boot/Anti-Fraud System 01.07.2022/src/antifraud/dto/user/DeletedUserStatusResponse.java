package antifraud.dto.user;

public class DeletedUserStatusResponse {

    private String username;

    private String status;

    public DeletedUserStatusResponse(String username, String status) {
        this.username = username;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }
}
