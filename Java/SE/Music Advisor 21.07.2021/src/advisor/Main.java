package advisor;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;


import java.util.Scanner;

public class Main {
    @Parameter(names = "-access")
    String access;

    @Parameter(names = "-resource")
    String resource;

    @Parameter(names = "-page")
    String page;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNext("auth")) {
            scanner.nextLine();
            System.out.println("Please, provide access for application.");
        }

        Main main = new Main();
        JCommander.newBuilder().addObject(main).build().parse(args);
        main.access = main.access == null ? "https://accounts.spotify.com" : main.access;
        new Application(main.resource == null ? "https://api.spotify.com" : main.resource,
                Integer.parseInt(main.page == null ? "5" : main.page),
                main.authorization()).run();

    }

    private String authorization() {
        return new Authorization(access).getAccessToken();
    }

}
