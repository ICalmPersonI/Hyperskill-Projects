package cinema.forms.responses;

import cinema.entities.Seat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PurchaseForm {
    private String token;
    @JsonProperty("ticket")
    private Seat seat;

    PurchaseForm() {

    }

    public PurchaseForm(String token, Seat seat) {
        this.token = token;
        this.seat = seat;
    }

    public String getToken() {
        return token;
    }

    public Seat getSeat() {
        return seat;
    }
}
