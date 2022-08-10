package platform.code;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "Codes")
@JsonPropertyOrder(value = {"code", "time", "views"})
public class Code {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @JsonIgnore
    private String id;

    private String code;

    private String date;

    private int time;

    private int views;

    @JsonIgnore
    private boolean timeLimit;

    @JsonIgnore
    private boolean viewLimit;

    @JsonCreator
    Code (String code, int time, int views) {
        this.code = code;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        this.time = time;
        this.views = views;
        this.timeLimit = time > 0;
        this.viewLimit = views > 0;
    }

    Code() {

    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public int getViews() {
        return views;
    }

    public boolean isTimeLimit() {
        return timeLimit;
    }

    public boolean isViewLimit() {
        return viewLimit;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
