package engine.forms;


public class Reply {

    private boolean success;

    private String feedback;

    public Reply(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFeedback() {
        return feedback;
    }
}
