package advisor;


import java.util.*;


public class View {

    private final List<String> printQueue = new ArrayList<>();
    private int currentPage = 1;
    private final int RECORDS_PER_PAGE;
    private int start = 0;

    View(int recordsPerPage) {
        this.RECORDS_PER_PAGE = recordsPerPage;
    }

    public void addToQueue(List<String> text) {
        printQueue.addAll(text);
        print();
    }

    private void print() {
        try {
            for (int i = start; i < start + RECORDS_PER_PAGE; i++) {
                System.out.println(printQueue.get(i));
            }
        } catch (IndexOutOfBoundsException e) {
        }
        System.out.printf("---PAGE %d OF %d---\n", currentPage, printQueue.size() / RECORDS_PER_PAGE);
    }

    public void prev() {
        if (currentPage - 1 > -1 && start - RECORDS_PER_PAGE > -1) {
            currentPage--;
            start = start - RECORDS_PER_PAGE;
            print();
        } else {
            System.out.println("No more pages.");
        }
    }

    public void next() {
        if (currentPage + 1 < printQueue.size() + 1) {
            currentPage++;
            start = start + RECORDS_PER_PAGE;
            print();
        } else {
            System.out.println("No more pages.");
        }
    }

}
