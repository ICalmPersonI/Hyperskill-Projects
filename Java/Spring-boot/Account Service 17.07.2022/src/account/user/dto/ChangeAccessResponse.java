package account.user.dto;

public class ChangeAccessResponse {

    private String status;

    public ChangeAccessResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
