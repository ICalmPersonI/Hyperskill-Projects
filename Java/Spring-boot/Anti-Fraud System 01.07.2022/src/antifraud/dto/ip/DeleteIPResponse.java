package antifraud.dto.ip;

public class DeleteIPResponse {

    private String status;

    public DeleteIPResponse(String ip) {
        this.status = String.format("IP %s successfully removed!", ip);
    }

    public String getStatus() {
        return status;
    }
}
