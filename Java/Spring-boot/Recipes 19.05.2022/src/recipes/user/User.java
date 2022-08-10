package recipes.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;

@Data
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotEmpty
    @NotNull
    @NotBlank
    @Pattern(regexp = "^.+@\\w+\\.\\w+$")
    private String email;

    @NotEmpty
    @NotNull
    @NotBlank
    @Size(min = 8)
    private String password;
}
