package account.payment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PaymentDataRequest {

    @NotEmpty
    private String employee;

    @NotNull
    private String period;

    @Min(value = 0L, message = "The value must be positive!")
    private long salary;

    @JsonCreator
    public PaymentDataRequest(@JsonProperty("employee") String employee,
            @JsonProperty("period") String period,
            @JsonProperty("salary") long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    public String getEmployee() {
        return employee;
    }

    public String getPeriod() {
        return period;
    }

    public long getSalary() {
        return salary;
    }
}
