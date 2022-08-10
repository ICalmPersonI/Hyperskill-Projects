package account.user.dto;


public class UserInfo {

    private long id;

    private String name;

    private String lastname;

    private String email;

    private String[] roles;

    public UserInfo(long id, String name, String lastname, String email, String[] roles) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLastname() {
        return lastname;
    }

    public String[] getRoles() {
        return roles;
    }
}
