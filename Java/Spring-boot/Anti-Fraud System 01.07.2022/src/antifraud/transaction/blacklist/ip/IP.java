package antifraud.transaction.blacklist.ip;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class IP {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @NotBlank
    private String ip;

    @JsonCreator
    IP(String ip) {
        this.ip = ip;
    }

    IP() {}

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }
}
