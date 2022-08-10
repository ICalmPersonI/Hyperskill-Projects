package cinema.forms.responses;

import cinema.entities.Seat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReturnForm {
    @JsonProperty("returned_ticket")
    private Seat seat;

    ReturnForm() {

    }

    public ReturnForm(Seat seat) {
        this.seat = seat;
    }

    public Seat getSeat() {
        return seat;
    }
}
