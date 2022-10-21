package calculator;


public class Main {

    public static void main(String[] args) {
        Calculator.execute();
    }
}

class Calculator {
    public static void execute() {
        while (true) {
            SmartCalculator.input();
        }
    }
}
