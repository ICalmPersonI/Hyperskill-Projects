package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

class BankCardManager {

    private static String DataBase;
    private static String loggedAcc;

    public static void setDataBase(String dataBase) {
        DataBase = dataBase;
    }

    private static String generateCardNumber() {
        List<Integer> result = new ArrayList<>();
        while (true) {
            List<Integer> cardNumber = new ArrayList<>();
            cardNumber.add(4);
            for (int i = 1; i < 16; i++) {
                int temp = i > 5 ? new Random().nextInt(9 - 1 + 1) + 1 : 0;
                cardNumber.add(temp);
            }

            if (luhnAlgorithm(cardNumber)) {
                result.addAll(cardNumber);
                break;
            }
        }
        return result.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static boolean luhnAlgorithm(List<Integer> list) {
        List<Integer> cardNumber = new ArrayList<>(list);
        int dropLast = cardNumber.remove(15);

        int count = 0;
        ListIterator<Integer> listIteratorMult = cardNumber.listIterator();
        while (listIteratorMult.hasNext()) {
            int temp = listIteratorMult.next();
            if (count % 2 == 0) {
                listIteratorMult.set(temp * 2);
            }
            count++;
        }

        ListIterator<Integer> listIteratorSub = cardNumber.listIterator();
        while (listIteratorSub.hasNext()) {
            int temp = listIteratorSub.next();
            if (temp > 9) {
                listIteratorSub.set(temp - 9);
            }
        }

        int sum = cardNumber.stream().
                mapToInt(Integer::intValue)
                .sum() + dropLast;

        return sum % 10 == 0;
    }

    static void create() {
        String cardNumber = generateCardNumber();
        String pin = String.valueOf(new Random().nextInt(9999 - 1000 + 1) + 1000);

        System.out.print("\nYour card has been created\n" +
                "Your card number:\n" +
                cardNumber +
                "\nYour card PIN:\n" +
                pin + "\n");

        String url = "jdbc:sqlite:F:/Simple Banking System/Simple Banking System/task/" + DataBase;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                int id;
                try (ResultSet resultSet = statement.executeQuery("SELECT id FROM card")) {
                    int maxId = 0;
                    while (resultSet.next()) {
                        int getId = resultSet.getInt("id");
                        if (getId > maxId) maxId = getId;
                    }
                    id = maxId + 1;
                }
                statement.executeUpdate("INSERT INTO card VALUES" +
                        "(" + id + "," +
                        " '" + cardNumber +
                        "', '" + pin + "', 0)");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addIncome() {
        System.out.println("\nEnter income:");
        int addIncome = new Scanner(System.in).nextInt();
        String url = "jdbc:sqlite:F:/Simple Banking System/Simple Banking System/task/" + DataBase;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("UPDATE card SET " +
                        "balance = balance + " + addIncome +
                        " WHERE number = " + loggedAcc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Income was added!\n");
    }

    static void doTransfer() {
        System.out.println("Transfer\n" +
                "Enter card number:");
        String cardNumber = new Scanner(System.in).nextLine();
        String[] split = cardNumber.split("");
        List<Integer> list = new ArrayList<>();
        Arrays.stream(split).forEach(s -> list.add(Integer.parseInt(s)));
        if (!(luhnAlgorithm(list))) {
            System.out.println("Probably you made a mistake in the card number. Please try again!\n");
            return;
        }
        if (!(findCardNumber(cardNumber))) {
            System.out.println("Such a card does not exist.");
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        int quanti = new Scanner(System.in).nextInt();

        String url = "jdbc:sqlite:F:/Simple Banking System/Simple Banking System/task/" + DataBase;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT balance FROM card WHERE number = "
                        + loggedAcc)) {
                    int balance = resultSet.getInt("balance");
                    if (balance - quanti < 0) {
                        System.out.println("Not enough money!");
                        return;
                    } else {
                        statement.executeUpdate("UPDATE card SET " +
                                "balance = balance - " + quanti +
                                " WHERE number = " + loggedAcc);
                        statement.executeUpdate("UPDATE card SET " +
                                "balance = " + quanti +
                                " WHERE number = " + cardNumber);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void closeAccount() {
        String url = "jdbc:sqlite:F:/Simple Banking System/Simple Banking System/task/" + DataBase;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM card WHERE number = " + loggedAcc);
                loggedAcc = "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void balance() {
        String url = "jdbc:sqlite:F:/Simple Banking System/Simple Banking System/task/" + DataBase;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT balance FROM card WHERE number = "
                        + loggedAcc)) {
                    int balance = resultSet.getInt("balance");
                    System.out.printf("Balance: %d%n", balance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static boolean login() {
        System.out.println("\nEnter your card number:");
        String card = new Scanner(System.in).nextLine();
        System.out.println("Enter your PIN:");
        String pin = new Scanner(System.in).nextLine();
        boolean logged = false;

        String url = "jdbc:sqlite:F:/Simple Banking System/Simple Banking System/task/" + DataBase;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT number, pin FROM card")) {
                    while (resultSet.next()) {
                        String cardNumber = resultSet.getString("number");
                        String pinNumber = resultSet.getString("pin");
                        if (card.matches(cardNumber) && pin.matches(pinNumber)) {
                            logged = true;
                            loggedAcc = cardNumber;
                            break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logged;
    }
    static boolean findCardNumber(String cardNumber) {
        boolean found = false;
        String url = "jdbc:sqlite:F:/Simple Banking System/Simple Banking System/task/" + DataBase;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT number FROM card")) {
                    while (resultSet.next()) {
                        String getCardNumber = resultSet.getString("number");
                        if (getCardNumber.matches(cardNumber)) {
                            found = true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return found;
    }

    static void loggOut() {
        System.out.println("You have successfully logged out!");
        loggedAcc = "";
    }
}
