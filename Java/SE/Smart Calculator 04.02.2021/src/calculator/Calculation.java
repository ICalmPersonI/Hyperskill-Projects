package calculator;

import java.math.BigInteger;
import java.util.*;

class Calculation {
    static void execute(String[] splitLine) {
        String[] line = getExpression(splitLine).split("\\s++");
        System.out.println(counting(line));
    }

    static private BigInteger counting(String[] input) {
        Deque<BigInteger> temp = new ArrayDeque<>();
        for (String s : input) {
            if (s.matches("\\d++")) {
                temp.push(new BigInteger(s));
            } else if (s.matches("\\D++")) {
                BigInteger a = temp.pop();
                BigInteger b = temp.pop();
                BigInteger result = null;
                switch (s) {
                    case "+":
                        result = b.add(a);
                        break;
                    case "-":
                        result = b.subtract(a);
                        break;
                    case "*":
                        result = b.multiply(a);
                        break;
                    case "/":
                        result = b.divide(a);
                        break;
                    case "^":
                        //result = Math.pow(b, a);
                        break;
                }
                temp.push(result);
            }
        }
        return temp.peek();
    }

    static private String getExpression(String[] splitLine) {
        Deque<String> operStack = new ArrayDeque<>();
        StringBuilder output = new StringBuilder();
        for (String value : splitLine) {
            if (value.matches("\\d++")) {
                output.append(value).append(" ");
            }
            if (value.matches("[\\D]++")) {
                if (value.matches("[(]++")) operStack.push(value);
                else if (value.matches("[)]++")) {
                    String s = operStack.pop();
                    while (!(s.matches("[(]++"))) {
                        output.append(s).append(" ");
                        s = operStack.pop();
                    }
                } else {
                    if (operStack.size() > 0)
                        if (getPriority(value) <= getPriority(operStack.peek()))
                            output.append(operStack.pop()).append(" ");

                    operStack.push(value);
                }
            }
        }
        while (operStack.size() > 0) output.append(operStack.pop()).append(" ");
        return output.toString();
    }

    static private int getPriority(String s) {
        switch (s)
        {
            case "(": return 0;
            case ")": return 1;
            case "+": return 2;
            case "-": return 3;
            case "*": return 4;
            case "/": return 4;
            case "^": return 5;
            default: return 6;
        }
    }
}
