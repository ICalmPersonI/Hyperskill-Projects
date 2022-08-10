package calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SmartCalculator {
    private static final Pattern EXACTLY = Pattern.compile("[=]++");
    private static final Pattern OPERATORS = Pattern.compile("([+]|[-]|[*]|[/]|[(]|[)])");
    private static final Pattern ASSIGNMENT_VARIABLES = Pattern.compile("[A-z]++[\\s]*[=]*");
    private static final Pattern COMMAND = Pattern.compile("[/][A-z]++");
    private static final Pattern PARENTHESES = Pattern.compile("[(]|[)]");
    private static final Pattern PARENTHESES_CHECK = Pattern.compile("([(][\\s|\\d|\\D]*[)])");

    static void input() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        Matcher matcherCommand = COMMAND.matcher(line);
        if (matcherCommand.find()) {
            command(line);
            return;
        }
        Matcher matcherAssignmentVariables = ASSIGNMENT_VARIABLES.matcher(line);
        Matcher matcherOperators = OPERATORS.matcher(line);
        Matcher matcherExactly = EXACTLY.matcher(line);
        if (matcherAssignmentVariables.find()) {
            if (matcherOperators.find()) {
                if (!matcherExactly.find()) {
                    Variables.preparingVariablesForCalculation(line);
                }
            }
            Variables.inputValidation(line);
            return;
        }
        Matcher matcherParentheses = PARENTHESES.matcher(line);
        Matcher matcherParenthesesCheck = PARENTHESES_CHECK.matcher(line);
        if (matcherOperators.find()) {
            if (matcherParentheses.find()) {
                if (matcherParenthesesCheck.find()) {
                    PreparingNumbersForCalculation.execute(line);
                } else {
                    System.out.println("Invalid expression");
                    return;
                }
            } else {
                PreparingNumbersForCalculation.execute(line);
            }
        }
    }

    private static void command(String line) {
        String[] command = {"/help", "/exit"};
        boolean availableCommand = false;
        for (String s : command) {
            if (line.matches(s)) {
                availableCommand = true;
                break;
            }
        }
        if (availableCommand) {
            if (line.matches("/help")) {
                System.out.println("The program calculates the sum/sub/div/mul of numbers");
            }
            if (line.matches("/exit")) {
                System.out.println("Bye!");
                System.exit(0);
            }
        } else System.out.println("Unknown command");
    }
}

class PreparingNumbersForCalculation {
    static void execute(String line) {
        int countMult = 0;
        int countDivide = 0;
        for (int i = 0; i < line.length(); i++) {
            if (String.valueOf(line.charAt(i)).matches("[*]")) {
                countMult++;
            } else countMult = 0;
            if (String.valueOf(line.charAt(i)).matches("[/]")) {
                countDivide++;
            } else countDivide = 0;
            if (countMult > 1 || countDivide > 1 ) {
                System.out.println("Invalid expression");
                return;
            }
        }
        line = line.replaceAll("[-]{2}[ ]|[ ][-]{2}", " + ")
                .replaceAll("[+]++", " + ")
                .replaceAll("[*]++", " * ")
                .replaceAll("[/]++", " / ")
                .replaceAll("[-]++[\\D]", " - ")
                .replaceAll("[+]++[\\s]++[-]", " - ")
                .replaceAll("[(]", " ( ")
                .replaceAll("[)]", " ) ")
                .replaceAll("([\\s]*[+][\\s]*)++", " + ")
                .replaceAll("\\s++", " ");
        String[] splitLine = line.split(" ");
        Calculation.execute(splitLine);
    }
}

class Variables {
    private static final Pattern EXACTLY = Pattern.compile("[=]++");
    private static final Pattern SHOW_VALUE = Pattern.compile("([\\s]*[A-z]++[\\s]*)");
    private static final Pattern IDENTIFIER = Pattern.compile("^([\\s]*[A-z]++[\\s]*[=]++)");
    private static final String ASSIGNMENT = "^([\\s]*[A-z]++[\\s]*[=]++[\\s]*[+|-]*[A-z]++[\\s]*)$" +
            "|^([\\s]*[A-z]++[\\s]*[=]++[\\s]*[+|-]*[\\d]++[\\s]*)$";
    private static final Map<String, BigInteger> listVariables = new TreeMap<>();
    private static final String ERROR = "Unknown variable";

    static void inputValidation(String line) {
        Matcher identifier = IDENTIFIER.matcher(line);
        Matcher exactly = EXACTLY.matcher(line);
        Matcher show = SHOW_VALUE.matcher(line);
        if (!exactly.find()) {
            if (show.find()) {
                showValue(line.replaceAll("\\s++", ""));
                return;
            }
        }
        if (identifier.find()) {
            if (line.matches(ASSIGNMENT)) {
                fillingMapWithVariables(line.replaceAll("\\s++", ""));
            } else System.out.println("Invalid assignment");
        } else System.out.println("Invalid identifier");
    }

    static void showValue(String line) {
        if (line.matches("^[A-z]++$")) {
            BigInteger temp = listVariables.getOrDefault(line, new BigInteger(String.valueOf(Integer.MIN_VALUE)));
            if (!temp.equals(new BigInteger(String.valueOf(Integer.MIN_VALUE)))) {
                System.out.println(temp);
            } else {
                System.out.println(ERROR);
                return;
            }
        }
    }

    private static void fillingMapWithVariables(String line) {
        line = line.replaceAll("[=]++", " = ")
                .replaceAll("\\s++", " ");
        String[] splitLine = line.split(" ");
        BigInteger value = null;
        boolean availability = false;
        if (!splitLine[2].matches("[-]*[\\d]++")) {
            for (String s : listVariables.keySet()) {
                if (s.matches(splitLine[2])) {
                    availability = true;
                    value = listVariables.get(splitLine[2]);
                    break;
                }
            }
        } else {
            availability = true;
            value = new BigInteger(splitLine[2]);
        }
        if (!availability) {
            System.out.println(ERROR);
            return;
        } else listVariables.put(splitLine[0], value);
    }

    static void preparingVariablesForCalculation(String line) {
        line = line.replaceAll("[-]{2}[ ]|[ ][-]{2}", " + ")
                .replaceAll("[+]++", " + ")
                .replaceAll("[*]++", " * ")
                .replaceAll("[/]++", " / ")
                .replaceAll("[-]++", " - ")
                .replaceAll("[+]++[\\s]++[-]", " - ")
                .replaceAll("[(]", " ( ")
                .replaceAll("[)]", " ) ")
                .replaceAll("([\\s]*[+][\\s]*)++", " + ")
                .replaceAll("\\s++", " ");
        String[] splitLine = line.split(" ");
        for (int i = 0; i < splitLine.length; i++) {
            if (splitLine[i].matches("[A-z]++")) {
                splitLine[i] = String.valueOf(listVariables.get(splitLine[i]));
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(splitLine[0]);
        for (int i = 1; i < splitLine.length; i++) {
            stringBuilder.append(" ");
            stringBuilder.append(splitLine[i]);
        }
        PreparingNumbersForCalculation.execute(stringBuilder.toString());
    }
}

