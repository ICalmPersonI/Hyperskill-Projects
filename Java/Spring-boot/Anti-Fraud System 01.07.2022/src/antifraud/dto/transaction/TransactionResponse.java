package antifraud.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"status", "info"})
public class TransactionResponse {
    @JsonProperty("result")
    private String status;

    private String info;

    public TransactionResponse(String status, String info) {
        this.status = status;
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }
}
