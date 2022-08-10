package cinema;

import cinema.entities.Cinema;
import cinema.entities.Error;
import cinema.entities.Seat;
import cinema.entities.Stats;
import cinema.forms.requests.PurchaseForm;
import cinema.forms.requests.ReturnForm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class Controller {

    private final int TOTAL_ROWS = 9;
    private final int TOTAL_COLUMNS = 9;
    private final Cinema cinema;

    Controller() {
        Seat[] seats = new Seat[TOTAL_ROWS * TOTAL_COLUMNS];
        for (int index = 0, rowIndex = 0; index < seats.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < TOTAL_COLUMNS; columnIndex++, index++) {
                seats[index] = new Seat(UUID.randomUUID(), rowIndex + 1, columnIndex + 1, rowIndex <= 4 ? 10 : 8);
            }
        }
        cinema = new Cinema(TOTAL_ROWS, TOTAL_COLUMNS, seats);
    }

    @GetMapping("seats")
    Cinema get() {
        return cinema;
    }

    @PostMapping("purchase")
    ResponseEntity<?> purchase(@RequestBody PurchaseForm form) {
        Object response = cinema.bookSeat(form.getRow(), form.getColumn());
        return new ResponseEntity(
                response,
                new HttpHeaders(),
                response instanceof Error ? HttpStatus.BAD_REQUEST : HttpStatus.OK)
        ;
    }

    @PostMapping("return")
    ResponseEntity<?> returnTicket(@RequestBody ReturnForm form) {
        Object response = cinema.unbook(form.getToken());
        return new ResponseEntity(
                response,
                new HttpHeaders(),
                response instanceof Error ? HttpStatus.BAD_REQUEST : HttpStatus.OK
        );
    }

    @PostMapping("stats")
    ResponseEntity<?> stats(@RequestParam(required = false) String password) {
        Stats stats = cinema.getStats();
        return password == null || !password.equals("super_secret") ?
                new ResponseEntity(new Error("The password is wrong!"), new HttpHeaders(), HttpStatus.UNAUTHORIZED) :
                new ResponseEntity(stats, new HttpHeaders(), HttpStatus.OK);

    }
}
