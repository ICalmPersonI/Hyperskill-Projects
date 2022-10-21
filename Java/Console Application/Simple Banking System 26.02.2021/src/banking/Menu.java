package banking;

import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

class Menu {
    private static String DataBase;

    static void createDB(String db) {
        DataBase = db;
        BankCardManager.setDataBase(db);
        File file = new File("F:\\Simple Banking System\\Simple Banking System\\task\\" + db);
        try {
            file.createNewFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        String url = "jdbc:sqlite:F:/Simple Banking System/Simple Banking System/task/" + db;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try(Connection connection = dataSource.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0);");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void mainMenu() {
        String options = "\n1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit\n";
        System.out.print(options);
        int select = new Scanner(System.in).nextInt();
        switch (select) {
            case 1:
                BankCardManager.create();
                break;
            case 2:
                if (BankCardManager.login()) {
                    System.out.println("\nYou have successfully logged in!");
                    loggedMenu();
                } else {
                    System.out.println("\nWrong card number or PIN!");
                }
                break;
            case 0:
                //dropTable();
                System.exit(0);
        }
    }

    private static void loggedMenu() {
        while (true) {
            String options = "1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit\n";
            System.out.print(options);
            int select = new Scanner(System.in).nextInt();
            switch (select) {
                case 1:
                    BankCardManager.balance();
                    break;
                case 2:
                    BankCardManager.addIncome();
                    break;
                case 3:
                    BankCardManager.doTransfer();
                    break;
                case 4:
                    BankCardManager.closeAccount();
                    return;
                case 5:
                    BankCardManager.loggOut();
                    return;
                case 0:
                    //dropTable();
                    System.exit(0);
            }
        }
    }

    private static void showTable() {
        String url = "jdbc:sqlite:F:/Simple Banking System/Simple Banking System/task/" + DataBase;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT * FROM card" )) {
                    System.out.println("Id "  + " Card " + " Pin " + " Balance ");
                    while (resultSet.next()) {
                        int getId = resultSet.getInt("id");
                        String getCard = resultSet.getString("number");
                        String getPin = resultSet.getString("pin");
                        int balance = resultSet.getInt("balance");
                        System.out.println(getId + " " + getCard + " " + getPin + " " + balance);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void dropTable() {
        String url = "jdbc:sqlite:F:/Simple Banking System/Simple Banking System/task/" + DataBase;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DROP TABLE card;");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
