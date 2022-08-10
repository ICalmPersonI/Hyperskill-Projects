package antifraud.dto.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class TransactionFeedback {
    private long transactionId;
    @NotEmpty
    @NotNull
    @Pattern(regexp = "^(ALLOWED)|(MANUAL_PROCESSING)|(PROHIBITED)$")
    private String feedback;

    @JsonCreator
    public TransactionFeedback(@JsonProperty("transactionId") long transactionId,
                               @JsonProperty("feedback") String feedback) {
        this.transactionId = transactionId;
        this.feedback = feedback;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public String getFeedback() {
        return feedback;
    }
}
