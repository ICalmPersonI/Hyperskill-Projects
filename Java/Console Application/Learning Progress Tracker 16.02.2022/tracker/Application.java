package tracker;

import tracker.data.DataStore;

import java.util.*;
import java.util.regex.Pattern;

public class Application {

    private final Scanner scanner = new Scanner(System.in);
    private final DataStore dataStore = new DataStore();

    private final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9.]+@[A-Za-z0-9.]+\\.\\w+$");
    private final Pattern FIRST_NAME = Pattern.compile("^(([A-Za-z]{2,})|([A-Za-z]+[\\-'][A-Za-z]+))+$");
    private final Pattern LAST_NAME = Pattern.compile("^(([A-Za-z]{2,})|([A-Za-z]+[\\-'\\s][A-Za-z]+)[\\s-]*)+$");
    private final Pattern WITHSPACE = Pattern.compile("\\s");

    public static final List<String> COURSES = List.of("java", "dsa", "databases", "spring");

    public void run() {
        System.out.println("Learning Progress Tracker");
        while (true) {
            String command = scanner.nextLine();

            if (command.isBlank()) {
                System.out.println("No input.");
            } else {
                switch (command) {
                    case "exit": {
                        System.out.println("Bye!");
                        System.exit(0);
                        break;
                    }
                    case "add students": {
                        addStudent();
                        break;
                    }
                    case "list": {
                        System.out.println(dataStore.getStudents());
                        break;
                    }
                    case "add points": {
                        addPoints();
                        break;
                    }
                    case "find": {
                        find();
                        break;
                    }
                    case "statistics": {
                        showStatistic();
                        break;
                    }
                    case "notify": {
                        HashMap<String, ArrayList<String>> certificates = dataStore.getCertificates();
                        if (!certificates.isEmpty())
                            certificates.values().forEach(arr -> arr.forEach(System.out::println));
                        System.out.printf("Total %d students have been notified\n", certificates.size());
                        break;
                    }
                    case "back": {
                        System.out.println("Enter 'exit' to exit the program.");
                        break;
                    }
                    default: {
                        System.out.println("Error: unknown command!");
                    }
                }
            }
        }
    }

    private void addStudent() {
        int numberOfStudents = 0;
        System.out.println("Enter student credentials or 'back' to return:");
        while (true) {
            String[] input = scanner.nextLine().split(WITHSPACE.pattern());
            if (input.length != 0 && input[0].equals("back")) {
                System.out.printf("Total %d students have been added.\n", numberOfStudents);
                break;
            }

            if (input.length < 3) {
                System.out.println("Incorrect credentials");
                continue;
            }

            String name = input[0];
            String lastName = String.join(" ", Arrays.copyOfRange(input, 1, input.length - 1));
            String email = input[input.length - 1];

            if (FIRST_NAME.matcher(name).matches()) {
                if (LAST_NAME.matcher(lastName).matches()) {
                    if (EMAIL.matcher(email).matches()) {
                        if (dataStore.containEmail(email)) {
                            dataStore.addStudent(name, lastName, email);
                            System.out.println("The student has been added.");
                            numberOfStudents++;
                        } else {
                            System.out.println("This email is already taken.");
                        }
                    } else {
                        System.out.println("Incorrect email");
                    }
                } else {
                    System.out.println("Incorrect last name");
                }
            } else {
                System.out.println("Incorrect first name");
            }
        }
    }

    private void addPoints() {
        System.out.println("Enter an id and points or 'back' to return:");
        while (true) {
            String[] input = scanner.nextLine().split(WITHSPACE.pattern());
            if (input[0].equals("back"))
                break;
            try {
                Integer.parseInt(input[0]);
            } catch (NumberFormatException ignore) {
               System.out.printf("No student is found for id=%s\n", input[0]);
               continue;
            }
            try {
                int[] data = Arrays.stream(input).mapToInt(Integer::parseInt).toArray();
                if (data.length > 5 || Arrays.stream(data).anyMatch(elem -> elem < 0)) {
                    System.out.println("Incorrect points format.");
                    continue;
                }
                if (dataStore.addPoints(data[0], data[1], data[2], data[3], data[4]))
                    System.out.println("Points updated.");
                else
                    System.out.println("Id not found.");
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
                System.out.println("Incorrect points format.");
            }
        }
    }

    private void showStatistic() {
        System.out.println("Type the name of a course to see details or 'back' to quit:");
        System.out.println(dataStore.getSummary());
        while (true) {
            String course = scanner.nextLine().toLowerCase(Locale.ROOT);
            if (course.equals("back"))
                break;
            else if (COURSES.contains(course))
                System.out.println(dataStore.getSummaryByCourse(course));
            else
                System.out.println("Unknown course.");
        }
    }

    private void find() {
        System.out.println("Enter an id or 'back' to return");
        while (true) {
            String input = scanner.nextLine();
            if (!input.equals("back"))
                System.out.println(dataStore.find(Integer.parseInt(input)));
            else
                break;
        }
    }

}
