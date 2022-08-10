package antifraud.transaction.blacklist.card;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Card {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @NotBlank
    private String number;

    @JsonCreator
    Card(String number) {
        this.number = number;
    }

    Card() {}

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }
}
