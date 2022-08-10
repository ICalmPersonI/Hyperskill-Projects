package engine.forms;


import java.time.LocalDateTime;

public class CompletedPage {
    private int totalPages;

    private int totalElements;

    private Content[] content;

    public static class Content {
        private int id;

        private LocalDateTime completedAt;

        public Content(int id, LocalDateTime completedAt) {
            this.id = id;
            this.completedAt = completedAt;
        }

        public int getId() {
            return id;
        }

        public String getCompletedAt() {
            return completedAt.toString();
        }
    }

    public CompletedPage(int totalPages, int totalElements, Content[] content) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.content = content;
    }

    public CompletedPage() {
        this.totalPages = 0;
        this.totalElements = 0;
        this.content = null;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public Content[] getContent() {
        return content;
    }
}
