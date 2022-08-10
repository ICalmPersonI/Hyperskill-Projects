package antifraud.transaction.limit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transaction_limit")
public class Limit {

    @Id
    @GeneratedValue
    private int id;

    private long allowed;

    private long manual;

    Limit(long allowed, long manual) {
        this.allowed = allowed;
        this.manual = manual;
    }

    Limit() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAllowed() {
        return allowed;
    }

    public long getManual() {
        return manual;
    }
}
