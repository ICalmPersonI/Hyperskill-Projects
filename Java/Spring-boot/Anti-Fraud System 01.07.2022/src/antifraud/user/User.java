package antifraud.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@JsonPropertyOrder(value = {"id", "name", "username", "role"})
@JsonIgnoreProperties(value = {"password", "locked"}, allowSetters = true)
public class User {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @NotEmpty
    @NotNull
    private String name;

    @NotBlank
    @NotEmpty
    @NotNull
    private String username;

    @NotBlank
    @NotEmpty
    @NotNull
    private String password;

    private String role;

    private boolean locked = true;

    @JsonCreator
    User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    User() {

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
