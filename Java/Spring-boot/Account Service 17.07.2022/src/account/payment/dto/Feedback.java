package account.payment.dto;

public class Feedback {

    private String status;

    public Feedback(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
