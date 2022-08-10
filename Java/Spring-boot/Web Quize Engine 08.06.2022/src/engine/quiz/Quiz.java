package engine.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String title;

    @NotNull
    private String text;

    @NotNull
    @Size(min = 2)
    private String[] options;

    private int[] answer;

    private String author;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<CompletedQuiz> completedQuizList;

    @JsonCreator
    Quiz(@JsonProperty("title") String title,
         @JsonProperty("text") String text,
         @JsonProperty("options") String[] options,
         @JsonProperty("answer") int[] answer) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer == null ? new int[] {} : Arrays.stream(answer).sorted().toArray();
    }

    Quiz() {

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    @JsonIgnore
    public int[] getAnswer() {
        return answer;
    }

    @JsonIgnore
    public String getAuthor() {
        return author;
    }

    @JsonIgnore
    public List<CompletedQuiz> getCompletedQuizList() {
        return completedQuizList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCompletedQuizList(List<CompletedQuiz> completedQuizList) {
        this.completedQuizList = completedQuizList;
    }
}
