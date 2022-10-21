package contacts;

import java.time.LocalDateTime;

public class Organization extends Record{
    String address;

    Organization(String name, String number, String address, String type) {
        this.name = name;
        this.number = hasNumber.apply(number);
        this.address = address;
        this.type = type;
        this.created = LocalDateTime.now();
        this.lastEdit = LocalDateTime.now();
    }

    @Override
    public String getSurname() {
        return null;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        this.lastEdit = LocalDateTime.now();
    }
}
