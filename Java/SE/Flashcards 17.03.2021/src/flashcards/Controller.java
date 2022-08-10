package flashcards;

import java.io.*;
import java.util.*;

public class Controller {
    private final Scanner scanner = new Scanner(System.in);
    private final CardList cardList = new CardList();
    private  Map<String, Integer> errorCard = new HashMap<>();

    public Map<String, Integer> getErrorCard() {
        return errorCard;
    }

     void resetStats() {
        Map<String, Integer> temp = new HashMap<>();
        for (String s : errorCard.keySet()) {
            temp.put(s, 0);
        }
        errorCard = temp;
        System.out.println("Card statistics have been reset.");
        Menu.addNote("Card statistics have been reset.");
    }

    void cost() {
        Map<String, Integer> temp = new HashMap<>();
        for (String s : errorCard.keySet()) {
            temp.put(s, 1);
        }
        errorCard = temp;
        System.out.println("Card statistics have been reset.");
        Menu.addNote("Card statistics have been reset.");
    }

    void print() {
        errorCard.forEach((s, l) -> System.out.println(s + " " + l));
        System.out.println();
        for (Card card : cardList.iteration()) {
            System.out.println(card.getTERM());
            System.out.println(card.getDEFINITION());
        }
    }

    void add() {

        String term;
        System.out.print("The card:\n");
        Menu.addNote("The card:\n");
        term = scanner.nextLine();
        Menu.addNote(term);
        for (Card card : cardList.iteration()) {
            try {
                if (term.compareTo(card.getTERM()) == 0) {
                    System.out.printf("The card \"%s\" already exists.\n", term);
                    Menu.addNote(String.format("The card \"%s\" already exists.\n", term));
                    return;
                }
            } catch (NullPointerException ignored) {
            }
        }


        String definition;
        System.out.print("The definition of the card:\n");
        Menu.addNote("The definition of the card:\n");
        definition = scanner.nextLine();
        Menu.addNote(definition);
        for (Card card : cardList.iteration()) {
            try {
                if (card.getDEFINITION().compareTo(definition) == 0) {
                    System.out.printf("The definition \"%s\" already exists.\n", definition);
                    Menu.addNote(String.format("The definition \"%s\" already exists.\n", definition));
                    return;
                }
            } catch (NullPointerException ignored) {
            }
        }

        cardList.append(term, definition);
        System.out.printf("The pair (\"%s\":\"%s\") has been added.\n", term, definition);
        Menu.addNote(String.format("The pair (\"%s\":\"%s\") has been added.\n", term, definition));
    }

    void remove() {
        System.out.println("Which card?");
        Menu.addNote("Which card?");
        String term = scanner.nextLine();
        Menu.addNote(term);
        if (cardList.remove(term)) {
            errorCard.remove(term);
            System.out.println("The card has been removed.");
            Menu.addNote("The card has been removed.");
        } else {
            System.out.printf("Can't remove \"%s\": there is no such card.\n", term);
            Menu.addNote(String.format("Can't remove \"%s\": there is no such card.\n", term));
        }

    }

    void importCard(String path) {
        File file;
        if (path.length() == 0) {
            System.out.println("File name:");
            Menu.addNote("File name:");
            file = new File(scanner.nextLine());
            Menu.addNote(String.valueOf(file));
            if (!file.exists()) {
                System.out.println("File not found.");
                Menu.addNote("File not found.");
                return;
            }
        } else {
            file = new File(path);
        }

        int count = 0;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            int i;
            String term = "";
            String definition = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((i = fileInputStream.read()) != -1) {
                if (i == 10) {
                    if (term.length() == 0) {
                        term = stringBuilder.toString();
                    } else {
                        definition = stringBuilder.toString();
                    }
                    stringBuilder.setLength(0);
                } else {
                    if (i != 13) {
                        stringBuilder.append((char) i);
                    }
                    if ((term.length() > 1) && (definition.length() > 1)) {
                        cardList.append(term, definition);
                        term = "";
                        definition = "";
                        count++;
                    }
                }
            }
            cardList.append(term, definition);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("%s cards have been loaded.\n", count + 1);
        Menu.addNote(String.format("%s cards have been loaded.\n", count + 1));



        importError();
    }

    void importError() {
        File fileKeys = new File("F:\\keys.gc");
        File fileValues = new File("F:\\values.gc");

        List<String> keys = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(fileKeys)) {
            int i;
            StringBuilder stringBuilder = new StringBuilder();
            while ((i = fileInputStream.read()) != -1) {
                if (i == 10) {
                    keys.add(stringBuilder.toString());
                }
                if (i != 10) {
                    stringBuilder.append((char) i);
                }
            }
            keys.add(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream fileInputStream = new FileInputStream(fileValues)) {
            int i;
            while ((i = fileInputStream.read()) != -1) {
                char temp = ((char) i);
                String str = String.valueOf(temp);
                values.add(Integer.parseInt(str));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < values.size(); i++) {
            errorCard.put(keys.get(i), values.get(i));
        }


    }

    void exportCard(String path) {

        File file;
        System.out.println(path.length());
        if (path.length() == 0) {
            System.out.println("File name:");
            Menu.addNote("File name:");
            file = new File(scanner.nextLine());
            Menu.addNote(String.valueOf(file));
        } else {
            file = new File(path);
        }

        int count = 0;
        Card[] cards = cardList.iteration();
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, false)) {
            for (Card card : cards) {
                List<Byte> byteList = new ArrayList<>();
                byte[] term = card.getTERM().getBytes();
                for (int i = 0; i < term.length; i++) {
                    byteList.add(term[i]);
                }
                byteList.add((byte) 10);
                byte[] definition = card.getDEFINITION().getBytes();
                for (int i = 0; i < definition.length; i++) {
                    byteList.add(definition[i]);
                }
                byteList.add((byte) 10);
                Object[] toArray = byteList.toArray();
                byte[] bytes = new byte[toArray.length];
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = (byte) toArray[i];
                }
                fileOutputStream.write(bytes);
                count++;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("%s cards have been saved.\n", count);
        Menu.addNote(String.format("%s cards have been saved.\n", count));

        exportError();
    }

   private void exportError() {
        File fileKeys = new File("F:\\keys.gc");
        File fileValues = new File("F:\\values.gc");
        Set<String> set = errorCard.keySet();
        Collection<Integer> collection = errorCard.values();
        List<String> keys = new ArrayList<>(set);
        List<Integer> values = new ArrayList<>(collection);
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileKeys, false)) {
            for (String s : keys) {
                byte[] bytes = s.getBytes();
                fileOutputStream.write(bytes);
                fileOutputStream.write(10);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(fileValues, false)) {
            for (Integer i : values) {
                String string = String.valueOf(i);
                byte[] bytes = string.getBytes();
                fileOutputStream.write(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void ask() {
        System.out.println("How many times to ask?");
        Menu.addNote("How many times to ask?");
        int number = scanner.nextInt();
        Menu.addNote(String.valueOf(number));
        Card[] card = cardList.iteration();
        int count = 0;
        for (int i = card.length - 1; count < number; i--) {
            Scanner scanner = new Scanner(System.in);
            boolean found = false;
            System.out.printf("Print the definition of \"%s\"\n", card[i].getTERM());
            Menu.addNote(String.format("Print the definition of \"%s\"\n", card[i].getTERM()));
            String answer = scanner.nextLine();
            Menu.addNote(answer);
            String result = card[i].chekAnswer(answer);
            if (result.matches("Correct!")) {
                System.out.println(result);
                Menu.addNote(result);
            } else {
                for (Card check : cardList.iteration()) {
                    String temp = check.chekAnswer(answer);
                    if (temp.matches("Correct!")) {
                        System.out.printf("%s, but your definition is correct for \"%s\".\n", result, check.getTERM());
                        int countError = errorCard.getOrDefault(card[i].getTERM(), 0);
                        errorCard.put(card[i].getTERM(), countError + 1);
                        Menu.addNote(
                                String.format("%s, but your definition is correct for \"%s\".\n", result, check.getTERM()));
                        found = true;
                        break;
                    }

                }
            }
            if (!found) {
                if (!result.matches("Correct!")) {
                    System.out.println(result);
                    Menu.addNote(result);
                    int countError = errorCard.getOrDefault(card[i].getTERM(), 0);
                    errorCard.put(card[i].getTERM(), countError + 1);
                }
            }
            count++;
        }

    }


}

class Card {

    private final String TERM;
    private final String DEFINITION;

    Card(String term, String definition) {
        this.TERM = term;
        this.DEFINITION = definition;
    }

    String chekAnswer(String answer) {
        int compare = DEFINITION.compareTo(answer);
        return compare == 0 ? "Correct!" : String.format("Wrong. The right answer is \"%s\".", DEFINITION);
    }

    public String getTERM() {
        return TERM;
    }

    public String getDEFINITION() {
        return DEFINITION;
    }
}

class CardList {

    private static Card[] list;

    CardList() {
        list = new Card[1];
    }

    public Card[] iteration() {
        return list;
    }

    public void append(String term, String definition) {
        int index = find();
        list[index] = new Card(term, definition);

    }

    public Card get(String term) {
        int index = findIndexByValue(term);
        return list[index];

    }

    public boolean remove(String term) {
        int index = findIndexByValue(term);
        if (index != -1) {
            Card[] temp = new Card[list.length - 1];
            for (int i = 0, a = 0; i < list.length; i++) {
                if (i != index) {
                    temp[a] = list[i];
                    a++;
                }
            }
            downscale(temp);
            return true;
        } else {
            return false;
        }
    }

    private int findIndexByValue(String value) {
        try {
            for (int i = 0; i < list.length; i++) {
                if (value.compareTo(list[i].getTERM()) == 0) {
                    return i;
                }
            }
        } catch (NullPointerException exception) {
            return -1;
        }
        return -1;
    }

    private int find() {
        boolean found = false;
        int index = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] == null) {
                index = i;
                found = true;
            }
        }
        if (found) {
            return index;
        } else {
            increaseSize();
            return find();
        }
    }

    private void downscale(Card[] cards) {
        if (list.length == 1) {
            list[0] = null;
        } else {
            list = new Card[cards.length];
            System.arraycopy(cards, 0, list, 0, cards.length);
        }
    }

    private void increaseSize() {
        Card[] temp = list.clone();
        list = new Card[list.length + 1];
        System.arraycopy(temp, 0, list, 0, temp.length);
    }

}
