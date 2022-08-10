package cinema.entities;

import cinema.forms.responses.PurchaseForm;
import cinema.forms.responses.ReturnForm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Cinema {
    @JsonProperty("total_rows")
    private int rows;
    @JsonProperty("total_columns")
    private int columns;
    @JsonProperty("available_seats")
    private Seat[] seats;

    private int totalIncome;

    private int availableSeats;

    private int purchasedTickets;


    public Cinema() {}

    public Cinema(int rows, int columns, Seat[] seats) {
        this.rows = rows;
        this.columns = columns;
        this.seats = seats;
        this.totalIncome = 0;
        this.availableSeats = seats.length;
        this.purchasedTickets = 0;
    }

    public Object bookSeat(int row, int column) {
        Seat seat = find(row, column);
        if (seat != null) {
            if (!seat.isAvailable()) {
                return new Error("The ticket has been already purchased!");
            }
            seat.book();
            totalIncome += seat.getPrice();
            availableSeats--;
            purchasedTickets++;
            return new PurchaseForm(seat.getToken(), seat);
        }
        return new Error("The number of a row or a column is out of bounds!");
    }

    public Object unbook(String token) {
        Seat seat = findByToken(token);
        if (seat != null) {
            if (!seat.isAvailable()) {
                seat.setAvailable(true);
                totalIncome -= seat.getPrice();
                availableSeats++;
                purchasedTickets--;
                return new ReturnForm(seat);
            }
        }
        return new Error("Wrong token!");
    }

    private Seat findByToken(String token) {
        for (Seat s: seats) {
            if (s.getToken().equals(token)) {
                return s;
            }
        }
        return null;
    }
    private Seat find(int row, int column) {
        for (Seat s: seats) {
            if (s.getRow() == row && s.getColumn() == column) {
                return s;
            }
        }
        return null;
    }

    @JsonIgnore
    public Stats getStats() {
        return new Stats(totalIncome, availableSeats, purchasedTickets);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Seat[] getSeats() {
        return seats;
    }
}
