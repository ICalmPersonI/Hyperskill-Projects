package advisor;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Application {

    private final Controller controller;

    Application(String resource, int page, String accessToken) {
        this.controller = new Controller(new Model(resource, accessToken), new View(page));
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        Pattern validInput = Pattern.compile("prev|next|exit|new|featured|categories|playlists [\\w\\s]+");
        String choice;
        while (true) {
            choice = scanner.nextLine();
            if (validInput.matcher(choice).matches()) {
                controller.updateView(choice);
            }
        }
    }
}
