package engine.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @NotEmpty
    @Pattern(regexp = ".+@.+\\..+")
    private String email;

    @NotNull
    @Size(min = 5)
    private String password;


    @JsonCreator
    User(@JsonProperty("email") String email,
         @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    User() {

    }


    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
