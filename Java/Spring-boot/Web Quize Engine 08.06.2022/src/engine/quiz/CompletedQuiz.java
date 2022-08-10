package engine.quiz;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
//@Table(name = "completed_quizzes")
public class CompletedQuiz {

    @Id
    @GeneratedValue
    private int id;

    private int userId;

    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private Quiz quiz;

    CompletedQuiz(int userId, LocalDateTime  completedAt, Quiz quiz) {
        this.userId = userId;
        this.completedAt = completedAt;
        this.quiz = quiz;
    }

    CompletedQuiz() {

    }

    @JsonProperty(value = "id")
    public int getId() {
        return this.quiz.getId();
    }

    @JsonIgnore
    public int getUserId() {
        return userId;
    }

    public LocalDateTime  getCompletedAt() {
        return completedAt;
    }

}
