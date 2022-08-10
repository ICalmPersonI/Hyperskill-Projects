package flashcards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Menu {
    private static final List<String> log = new ArrayList<>();

    public static void addNote(String note) {
        log.add(note);
    }

    public static void selectingAction(String[] arg) {
        Controller controller = new Controller();
        if (arg.length != 0) {
            for (int i = 0; i < arg.length; i++) {
                if (arg[i].matches("-import")) {
                    controller.importCard(arg[i + 1]);
                }
            }
        }
        while (true) {
            String choiceList = "\nInput the action (add, remove, import, export, ask," +
                    " exit, log, hardest card, reset stats):";
            System.out.println(choiceList);
            addNote(choiceList);
            String choice = new Scanner(System.in).nextLine();
            addNote(choice);
            switch (choice) {
                case "add":
                    controller.add();
                    break;
                case "remove":
                    controller.remove();
                    break;
                case "import":
                    controller.importCard("");
                    break;
                case "export":
                    controller.exportCard("");
                    break;
                case "ask":
                    controller.ask();
                    break;
                case "print":
                    controller.print();
                    break;
                case "log":
                    saveLog();
                    break;
                case "hardest card":
                    hardestCard(controller.getErrorCard());
                    break;
                case "reset stats":
                    controller.resetStats();
                    break;
                case "exit":
                    if (arg.length != 0) {
                        if (arg[0].matches("-export")) {
                            controller.exportCard(arg[1]);
                        }
                    }
                    log.add("Bye bye!");
                    System.out.println("Bye bye!");
                    System.exit(0);
                default:
                    //controller.importCard();
                    System.out.println("Wrong action!");
            }
        }
    }


    private static void hardestCard(Map<String, Integer> map) {
        Set<String> set = map.keySet();
        Collection<Integer> collection = map.values();
        List<String> keys = new ArrayList<>(set);
        List<Integer> values = new ArrayList<>(collection);
        List<Integer> indexes = new ArrayList<>();
        int max = 0;
        for (int i = 0; i < keys.size(); i++) {
            if (values.get(i) > max) {
                max = values.get(i);
            }
        }

        for (int i = 0; i < keys.size(); i++) {
            if (values.get(i) == max) {
                indexes.add(i);
            }
        }

        if (max == 0) {
            System.out.println("There are no cards with errors.");
            log.add("There are no cards with errors.");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (indexes.size() == 1) {
            stringBuilder.append("The hardest card is");
            stringBuilder.append(" \"");
            stringBuilder.append(keys.get(indexes.get(0))).append("\". ");
            stringBuilder.append(String.format("You have %s errors answering it.", max));
        } else {
            stringBuilder.append("The hardest cards are");
            for (int i = 0; i < indexes.size() - 1; i++) {
                stringBuilder.append(" \"");
                stringBuilder.append(keys.get(indexes.get(i))).append("\"").append(",");

            }
            stringBuilder.append("\"");
            stringBuilder.append(keys.get(indexes.get(indexes.size() -1 ))).append("\"");
        }
        System.out.println(stringBuilder.toString());
        log.add(stringBuilder.toString());

    }

    private static void saveLog() {
        System.out.println("File name:");
        Menu.addNote("File name:");
        File file = new File(new Scanner(System.in).nextLine());
        Menu.addNote(String.valueOf(file));
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, false)) {
            for (String logs : log) {
                byte[] bytes = logs.getBytes();
                fileOutputStream.write(bytes);
                fileOutputStream.write(10);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The log has been saved.");
        log.add("The log has been saved.");
    }
}
