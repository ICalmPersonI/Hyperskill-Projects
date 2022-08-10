package cinema.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class Seat {
    @JsonIgnore
    UUID token;
    private int row;
    private int column;
    private int price;

    @JsonIgnore
    private boolean available = true;

    public Seat() {}
    public Seat(UUID token, int row, int column, int price) {
        this.token = token;
        this.row = row;
        this.column = column;
        this.price = price;
    }

    public void book() {
        available = false;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getPrice() {
        return price;
    }

    public String getToken() {
        return token.toString();
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
