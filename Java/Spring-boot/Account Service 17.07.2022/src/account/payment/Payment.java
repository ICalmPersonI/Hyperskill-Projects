package account.payment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.YearMonth;

@Entity
public class Payment {

    @Id
    @GeneratedValue
    private long id;

    private String employee;

    private YearMonth period;

    private long salary;

    Payment(String employee, YearMonth period, long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    Payment() { }

    public long getId() {
        return id;
    }

    public String getEmployee() {
        return employee;
    }

    public YearMonth getPeriod() {
        return period;
    }

    public long getSalary() {
        return salary;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object.getClass() != this.getClass()) {
            return false;
        }

        Payment other = (Payment) object;

        return other.getEmployee().equals(this.employee)
                && other.getPeriod().equals(this.period)
                && other.getSalary() == this.salary;
    }
}
