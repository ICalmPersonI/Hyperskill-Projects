package search;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class SearchPeople {
    private static final Map<Integer, String> peopleList = new HashMap<>();
    private static final Map<String, Set<String>> wordList = new LinkedHashMap<>();
    private final String path;

    SearchPeople(String path) {
        this.path = path;
        fillingPeopleList();
        while (true) {
            menu();
        }
    }

    private void menu() {
        System.out.println("\n=== Menu ===\n" +
                "1. Find a person\n" +
                "2. Print all people\n" +
                "0. Exit");
        int choice = new Scanner(System.in).nextInt();

        switch (choice) {
            case 1:
                System.out.println("Select a matching strategy:");
                String select = new Scanner(System.in).next();
                switch (select) {
                    case "ALL":
                        all();
                        break;
                    case "ANY":
                        any();
                        break;
                    case "NONE":
                        none();
                        break;
                }

                break;
            case 2:
                System.out.println("\n=== List of people ===");
                peopleList.forEach((p, l) -> System.out.printf("\n%s", l));
                System.out.println();
                break;
            case 0:
                System.out.println("Bye!");
                System.exit(0);
            default:
                System.out.println("Incorrect option! Try again.");
        }

    }

    private void fillingPeopleList() {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            StringBuilder stringBuilder = new StringBuilder();
            int id = 0;

            int i;
            while ((i = fileInputStream.read()) != -1) {
                if (i == 10) {
                    peopleList.put(id, stringBuilder.toString());
                    id++;
                    stringBuilder.setLength(0);
                    continue;
                }
                stringBuilder.append((char) i);
            }
            peopleList.put(id, stringBuilder.toString());

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        for (String line : peopleList.values()) {
            String[] split = line.split("\\s");
            for (String splitLine : split) {
                Set<String> temp = new LinkedHashSet<>();
                Pattern pattern = Pattern.compile(splitLine, CASE_INSENSITIVE);
                for (String mapValues : peopleList.values()) {
                    Matcher matcher = pattern.matcher(mapValues);
                    if (matcher.find()) {
                        temp.add(String.valueOf(getKeysByValue(peopleList, mapValues)));
                        wordList.put(splitLine, temp);
                    }
                }
            }
        }
        wordList.forEach((a, s) -> System.out.println(a + " " + s));
    }

    private void all() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter a name or email to search all suitable people.");
        String dataSearchPeople = scanner.nextLine();
        Pattern pattern = Pattern.compile(dataSearchPeople, CASE_INSENSITIVE);
        List<String> foundPeople = new ArrayList<>(findPerson(pattern));
        view(foundPeople);
    }

    private void any() {
        Scanner scanner = new Scanner(System.in);
        Set<String> foundPeople = new HashSet<>();
        System.out.println("\nEnter a name or email to search all suitable people.");
        String dataSearchPeople = scanner.nextLine();
        String[] split = dataSearchPeople.split("\\s");
        for (String word : split) {
            Pattern pattern = Pattern.compile(word, CASE_INSENSITIVE);
            foundPeople.addAll(findPerson(pattern));
        }
        List<String> temp = new ArrayList<>(foundPeople);
        view(temp);
    }

    private void none() {
        Scanner scanner = new Scanner(System.in);

        Set<String> foundPeople = new HashSet<>(peopleList.values());
        System.out.println("\nEnter a name or email to search all suitable people.");
        String dataSearchPeople = scanner.nextLine();
        String[] split = dataSearchPeople.split("\\s");

        for (String word : split) {
            Iterator<String> iterator = foundPeople.iterator();
            Pattern pattern = Pattern.compile(word, CASE_INSENSITIVE);
            while (iterator.hasNext()) {
                String temp = iterator.next();
                Matcher matcher = pattern.matcher(temp);
                if (matcher.find()) {
                    iterator.remove();
                }
            }
        }

        List<String> temp = new ArrayList<>(foundPeople);
        view(temp);
    }

    private List<String> findPerson(Pattern pattern) {

        List<String> foundPeople = new ArrayList<>();
        for (String line : wordList.keySet()) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                for (String s : wordList.get(line)) {
                    boolean thereElement = false;

                    String temp = peopleList.get(Integer.parseInt(s.replaceAll("\\[", "")
                            .replaceAll("]", "")));

                    if (foundPeople.size() == 0) {
                        foundPeople.add(temp);
                    }

                    for (String f : foundPeople) {
                        if (f.matches(temp)) {
                            thereElement = true;
                            break;
                        }
                    }

                    if (!thereElement) {
                        foundPeople.add(temp);
                    }
                }
            }
        }

        return foundPeople;
    }

    private void view(List<String> foundPeople) {
        if (foundPeople.size() > 0) {
            System.out.printf("\n%d persons found:\n", foundPeople.size());
            foundPeople.forEach(System.out::println);
        } else {
            System.out.println("No matching people found.");
        }
    }


    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<T>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
}
