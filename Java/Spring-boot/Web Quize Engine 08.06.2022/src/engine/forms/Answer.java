package engine.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Answer {
    private int[] answer;

    @JsonCreator
    Answer(@JsonProperty("answer") int[] answer) {
        this.answer = answer;
    }

    public int[] getAnswer() {
        return answer;
    }
}
