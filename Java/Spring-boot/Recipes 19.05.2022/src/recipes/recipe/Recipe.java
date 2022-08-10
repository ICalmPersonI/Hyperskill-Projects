package recipes.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Recipes")
public class Recipe {
    @JsonIgnore
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String category;

    @LastModifiedDate
    private LocalDateTime date;

    @NotEmpty
    @NotBlank
    @NotNull
    private String description;

    @Size(min = 1)
    @NotNull
    private String[] ingredients;

    @Size(min = 1)
    @NotNull
    private String[] directions;

    @JsonIgnore
    private String author;
}
