package account.event;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Event {

    public static final String CREATE_USER = "CREATE_USER";
    public static final String CHANGE_PASSWORD = "CHANGE_PASSWORD";
    public static final String ACCESS_DENIED = "ACCESS_DENIED";
    public static final String LOGIN_FAILED = "LOGIN_FAILED";
    public static final String GRANT_ROLE = "GRANT_ROLE";
    public static final String REMOVE_ROLE = "REMOVE_ROLE";
    public static final String LOCK_USER = "LOCK_USER";
    public static final String UNLOCK_USER = "UNLOCK_USER";
    public static final String DELETE_USER = "DELETE_USER";
    public static final String BRUTE_FORCE = "BRUTE_FORCE";

    @Id
    @GeneratedValue
    private long id;

    private String date;

    private String action;

    private String subject;

    private String object;

    private String path;

    Event(String date, String action, String subject, String object, String path) {
        this.date = date;
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }

    Event() {

    }

    public String getDate() {
        return date;
    }

    public String getAction() {
        return action;
    }

    public String getSubject() {
        return subject;
    }

    public String getObject() {
        return object;
    }

    public String getPath() {
        return path;
    }
}
