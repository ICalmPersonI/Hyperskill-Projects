package contacts;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.function.UnaryOperator;

public class Person extends Record {
    String surname;
    String birthDate;
    String gender;

    Person(String name, String surname, String birthDate, String gender, String number, String type) {
        this.name = name;
        this.surname = surname;
        this.birthDate = hasBirthDate.apply(birthDate);
        this.gender = hasGender.apply(gender);
        this.number = hasNumber.apply(number);
        this.type = type;
        this.created = LocalDateTime.now();
        this.lastEdit = LocalDateTime.now();
    }

    @Override
    public String getName() {
        return String.format("%s %s", this.name, this.surname);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        this.lastEdit = LocalDateTime.now();
    }

    @Override
    public void setNumber(String number) {
        this.number = hasNumber.apply(number);
        this.lastEdit = LocalDateTime.now();
    }

    @Override
    public String getSurname() {
        return surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        this.lastEdit = LocalDateTime.now();
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = hasBirthDate.apply(birthDate);
        this.lastEdit = LocalDateTime.now();
    }

    public void setGender(String gender) {
        this.gender = hasGender.apply(gender);
        this.lastEdit = LocalDateTime.now();
    }

    private final transient UnaryOperator<String> hasBirthDate = string -> string.matches("f") ? string : "[no data]";

    private final transient UnaryOperator<String> hasGender = string ->
            string.matches("F") || string.matches("M") ? string : "[no data]";

}
