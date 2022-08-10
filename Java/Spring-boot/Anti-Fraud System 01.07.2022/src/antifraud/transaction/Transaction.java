package antifraud.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    @JsonProperty("transactionId")
    private long id;

    private long amount;

    @NotEmpty
    @NotNull
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
    private String ip;

    @NotEmpty
    @NotNull
    private String number;

    @NotEmpty
    @NotNull
    @Pattern(regexp = "^(EAP)|(ECA)|(HIC)|(LAC)(MENA)|(SA)|(SSA)$")
    private String region;

    @NotNull
    @JsonProperty("date")
    private LocalDateTime transactionDate;

    private String result;

    private String feedback;

    Transaction(@JsonProperty("amount") long amount,
                @JsonProperty("ip") String ip,
                @JsonProperty("number") String number,
                @JsonProperty("region") String region,
                @JsonProperty("date") String date) {
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.transactionDate = LocalDateTime.parse(date);
    }

    Transaction() {

    }

    public long getId() {
        return id;
    }

    public long getAmount() {
        return amount;
    }

    public String getIp() {
        return ip;
    }

    public String getNumber() {
        return number;
    }

    public String getRegion() {
        return region;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public String getResult() {
        return result;
    }

    public String getFeedback() {
        return feedback == null ? "" : feedback;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "id: " + this.id + "\n" +
                "amount: " + this.amount + "\n" +
                "ip: " + this.ip + "\n" +
                "number: " + this.number + "\n" +
                "region: " + this.region + "\n" +
                "date: " + this.transactionDate + "\n" +
                "result: " + this.feedback + "\n" +
                "feedback: " + this.feedback + "\n";
    }
}
