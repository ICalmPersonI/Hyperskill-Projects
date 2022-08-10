package account.user.dto;

public class Feedback {

    private String user;

    private String status;

    public Feedback(String user, String status) {
        this.user = user;
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }
}
