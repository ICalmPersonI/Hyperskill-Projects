package contacts;

import java.io.IOException;
import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException {
        String fileName = "";

        if (args.length > 0) {
            fileName = args[0];
        }
        new Application(fileName).run();
    }
}

class Application {

    private final PhoneBook phoneBook;

    Application(String file) {
        this.phoneBook = new PhoneBook(String.format("%s", file));
    }

    private final static String MENU = "\n[menu] Enter action (add, list, search, count, exit):";
    private final static String SEARCH = "\n[search] Enter action ([number], back, again):";
    private final static String RECORD = "\n[record] Enter action (edit, delete, menu):";
    private final static String LIST = "\n[list] Enter action ([number], back):";

    private final Scanner scanner = new Scanner(System.in);

    private final String[] MSG_LINES = {
            "Enter the name of the person:",
            "Enter the surname of the person:",
            "Enter the birth date:",
            "Enter the gender:",
            "Enter the number:",
            "Enter the organization name:",
            "Enter the address:",
            "Enter the number:"
    };

    private final Consumer<String> isEmptyMassage = str -> System.out.printf("No records to %s!", str);

    private final Supplier<Record> newPerson = () -> {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            System.out.print(MSG_LINES[i]);
            list.add(scanner.nextLine());
        }
        System.out.print("The record added.\n");
        return new Person(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), "person");
    };

    private final Supplier<Record> newOrganization = () -> {
        List<String> list = new ArrayList<>();
        for (int i = 5; i < MSG_LINES.length; i++) {
            System.out.println(MSG_LINES[i]);
            list.add(scanner.nextLine());
        }
        System.out.print("The record added.\n");
        return new Organization(list.get(0), list.get(2), list.get(1), "org");
    };


    public void run() {
        menu();
    }

    private void menu() {
        System.out.print("[menu] Enter action (add, list, search, count, exit):");
        while (true) {
            switch (scanner.nextLine()) {
                case "add":
                    add();
                    break;
                case "list":
                    list();
                    break;
                case "search":
                    search("again");
                    break;
                case "count":
                    System.out.printf("The Phone Book has %s records.\n", phoneBook.count());
                    break;
                case "exit":
                    phoneBook.save();
                    System.exit(0);
            }
            System.out.print(MENU);
        }
    }

    private void add() {
        Record record = null;
        boolean exit = false;
        do {
            System.out.println("Enter the type (person, organization):");
            switch (scanner.nextLine()) {
                case "person":
                    record = newPerson.get();
                    exit = true;
                    break;
                case "organization":
                    record = newOrganization.get();
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong action!");
            }
        } while (!exit);
        phoneBook.add(record);
    }

    private void list() {
        phoneBook.print();
        while (true) {
            System.out.print(LIST);
            String action = scanner.nextLine();
            if (action.matches("back")) {
                break;
            } else if (action.matches("\\d")) {
                Record record = phoneBook.get(Integer.parseInt(action));
                if (record != null) {
                    recordMenu(record);
                    break;
                }
            } else {
                System.out.println("Wrong action!");
            }
        }
    }

    private void search(String action) {
        Function<String, List<Record>> search = line ->
                new ArrayList<>(
                        phoneBook.search(line).values()
                );

        boolean exit = false;
        while (!exit) {
            switch (action) {
                case "again":
                    System.out.print("Enter search query:");
                    String query = scanner.nextLine();
                    List<Record> records = search.apply(query);
                    System.out.printf("\nFound %s results:\n", records.size());
                    IntStream.range(0, records.size())
                            .forEach(id -> System.out.printf("%s. %s\n", new Integer(id + 1), records.get(id).getName()));
                    System.out.print(SEARCH);
                    String recordName = scanner.nextLine();
                    if (!recordName.matches("\\d")) {
                        search(recordName);
                    } else {
                        Record record = records.get(Integer.parseInt(recordName));
                        if (record != null) {
                            recordMenu(record);
                            return;
                        } else {
                            System.out.println("Record not found!");
                        }
                    }
                    break;
                case "\\d":
                    break;
                case "back":
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong action!");
            }
            System.out.print(SEARCH);
            action = scanner.nextLine();
        }
    }

    private void recordMenu(Record record) {
        System.out.println(
                record.getType().matches("person")
                        ? infoPerson.apply((Person) record)
                        : infoOrganization.apply((Organization) record)
        );
        boolean exit = false;
        while (!exit) {
            System.out.print(RECORD);
            switch (scanner.nextLine()) {
                case "edit":
                    if (record.getClass() == Person.class) {
                        editPerson.apply((Person) record);
                    } else if (record.getClass() == Organization.class) {
                        editOrganization.apply((Organization) record);
                    }
                    break;
                case "delete":
                    phoneBook.remove(record.getName());
                    break;
                case "menu":
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong action!");
            }
        }
    }

    private final UnaryOperator<Person> editPerson = person -> {
        System.out.print("Select a field (name, surname, birth, gender, number):");
        switch (scanner.nextLine()) {
            case "name":
                System.out.print("\nEnter name:");
                person.setName(scanner.nextLine());
                break;
            case "surname":
                System.out.print("\nEnter surname:");
                person.setSurname(scanner.nextLine());
                break;
            case "number":
                System.out.print("\nEnter number:");
                person.setNumber(scanner.nextLine());
                break;
            case "birth":
                System.out.print("\nEnter the birth date:");
                person.setBirthDate(scanner.nextLine());
                break;
            case "gender":
                System.out.println("\nEnter the gender (M, F):");
                person.setGender(scanner.nextLine());
                break;
        }
        return person;
    };

    private final UnaryOperator<Organization> editOrganization = organization -> {
        System.out.print("Select a field (name, address, number):");
        switch (scanner.nextLine()) {
            case "name":
                System.out.print("\nEnter the name:");
                organization.setName(scanner.nextLine());
                break;
            case "address":
                System.out.print("\nEnter the address");
                organization.setAddress(scanner.nextLine());
                break;
            case "number":
                System.out.println("\nEnter the number");
                organization.setNumber(scanner.nextLine());
                break;
        }
        return organization;
    };

    private final Function<Person, String> infoPerson = person ->
            String.format("Name: %s\n" +
                            "Surname: %s\n" +
                            "Birth date: %s\n" +
                            "Gender: %s\n" +
                            "Number: %s\n" +
                            "Time created: %s\n" +
                            "Time last edit: %s",
                    person.getName(),
                    person.getSurname(),
                    person.getBirthDate(),
                    person.getGender(),
                    person.getNumber(),
                    person.getCreated().toString(),
                    person.getLastEdit().toString());

    private final Function<Organization, String> infoOrganization = organization ->
            String.format("Organization name: %s\n" +
                            "Address: %s\n" +
                            "Number: %s\n" +
                            "Time created: %s\n" +
                            "Time last edit: %s",
                    organization.getName(),
                    organization.getAddress(),
                    organization.getNumber(),
                    organization.getCreated(),
                    organization.getLastEdit());

}
