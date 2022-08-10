package engine.forms;

import engine.quiz.Quiz;

public class QuizPage {

    private int totalPages;

    private int totalElements;

    private Quiz[] content;

    public QuizPage(int totalPages, int totalElements, Quiz[] content) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.content = content;
    }

    public QuizPage() {
        this.totalPages = 0;
        this.totalElements = 0;
        this.content = new Quiz[] {};
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public Quiz[] getContent() {
        return content;
    }
}
