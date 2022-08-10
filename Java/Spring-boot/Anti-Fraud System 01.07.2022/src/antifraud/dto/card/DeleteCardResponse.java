package antifraud.dto.card;

public class DeleteCardResponse {

    private String status;

    public DeleteCardResponse(String cardNumber) {
        this.status = String.format("Card %s successfully removed!", cardNumber);
    }

    public String getStatus() {
        return status;
    }
}
