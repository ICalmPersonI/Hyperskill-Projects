package banking;

public class Main {
    public static void main(String[] args) {
        Menu.createDB(args[1]);
        while (true) {
            Menu.mainMenu();
        }
    }
}