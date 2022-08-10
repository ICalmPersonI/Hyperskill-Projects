package account.group;

import account.user.User;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "principle_groups")
public class Group {

    public static final String PREFIX_ROLE = "ROLE_";
    public static final String USER = "USER";
    public static final String ACCOUNTANT = "ACCOUNTANT";
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    public static final String AUDITOR = "AUDITOR";

    public static final List<Group> ADMIN_GROUP = List.of(
            new Group(PREFIX_ROLE + ADMINISTRATOR, "Admin group"));
    public static final List<Group> CUSTOMER_GROUP = List.of(
            new Group(PREFIX_ROLE + USER, "Customer group"),
            new Group(PREFIX_ROLE + ACCOUNTANT, "Customer group"),
            new Group(PREFIX_ROLE + AUDITOR, "Customer group"));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String name;

    @ManyToMany(mappedBy = "userGroups")
    private Set<User> users;

    public Group(String code, String name) {
        this.code = code;
        this.name = name;
    }

    Group() {

    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
