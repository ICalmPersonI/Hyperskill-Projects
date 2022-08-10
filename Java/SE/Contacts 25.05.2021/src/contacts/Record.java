package contacts;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.UnaryOperator;

public abstract class Record implements Serializable {
    String name;
    String number;
    String type;
    LocalDateTime created;
    LocalDateTime lastEdit;

    public String getName() {
        return this.name;
    }
    public String getNumber() {
        return this.number;
    }
    public String getType() {
        return this.type;
    }
    public LocalDateTime getCreated() {
        return this.created;
    }
    public LocalDateTime getLastEdit() {
        return this.lastEdit;
    }
    public abstract void setName(String name);


    public abstract String getSurname();
    public abstract void setNumber(String number);

    public final transient UnaryOperator<String> hasNumber = number -> Arrays.stream(number.split(""))
            .filter(f -> f.matches("[(]|[)]"))
            .count() <= 2
            && number
            .matches("^[+]?[\\d\\s]*[(]\\d+[)][\\s](\\d+[-|\\s]?\\d+-([A-z]+|[\\d]+))|(\\d{1,3})|" +
                    "^(\\d{3}(\\s|-)([A-z]{3}|\\d{3})(\\s|-)*([A-z]{3}|\\d{3})*(\\s|-)*([A-z]{3}|\\d{3})*)$|" +
                    "^(\\d{3}(\\s|-)\\d{0,4}(\\s|-)[A-z]{0,4}(\\s|-)\\d{0,4})$|" +
                    "^([+]?([(]?(\\d{0,3}|[a-zA-Z])[)]?)?(\\s)?([(]?(\\d{2,3}|[a-zA-Z]+)[)]?)(\\s|-)?([(]?\\d{2,3}[)]?)?(\\s|-)?([(]?\\d{2,3}[)]?)?)$")
            ? number : "[no number]";
}
