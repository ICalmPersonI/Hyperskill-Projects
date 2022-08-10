package numbers;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Number {
    private final long number;
    private final Map<String, Boolean> properties = new LinkedHashMap<>();
    private final String[] splitNumber;

    Number(long number) {
        this.number = number;
        this.splitNumber = String.valueOf(number).split("");
        boolean isHappy = isHappy();
        properties.put("buzz", isBuzz());
        properties.put("duck", isDuck());
        properties.put("palindromic", isPalindromic());
        properties.put("gapful", isGapful());
        properties.put("happy", isHappy);
        properties.put("sad", !isHappy);
        properties.put("spy", isSpy());
        properties.put("square", isSquare());
        properties.put("sunny", isSunny());
        properties.put("jumping", isJumping());
        properties.put("even", isEven());
        properties.put("odd", isOdd());
    }

    public long getNumber() {
        return number;
    }

    private boolean isEven() {
        return number % 2 == 0;
    }

    private boolean isOdd() {
        return number % 2 != 0;
    }

    private boolean isBuzz() {
        return number % 7 == 0
                | String.valueOf(number)
                .charAt(splitNumber.length - 1) == '7';
    }

    private boolean isDuck() {
        return Objects.equals(Stream.of(splitNumber)
                .skip(1)
                .filter(elem -> elem.charAt(0) == '0')
                .findFirst().orElse(null), "0");
    }

    private boolean isPalindromic() {
        return new StringBuilder(String.valueOf(number))
                .reverse()
                .toString()
                .matches(String.valueOf(number));
    }

    private boolean isGapful() {
        String stringNumber = String.valueOf(number);
        return stringNumber.length() > 2 & number % (Long.parseLong(String.valueOf(stringNumber.charAt(0))
                + String.valueOf(stringNumber.charAt(stringNumber.length() - 1)))) == 0;
    }

    private boolean isSpy() {
        return Stream.of(splitNumber).mapToInt(Integer::parseInt).sum()
                == Stream.of(splitNumber).mapToInt(Integer::parseInt).reduce(1, (f, s) -> f * s);
    }

    private boolean isSunny() {
        return number != 0 & Math.sqrt(number + 1) % 1 == 0;
    }

    private boolean isSquare() {
        return Math.sqrt(number) % 1 == 0;
    }

    private boolean isJumping() {
        AtomicBoolean isJumping = new AtomicBoolean(true);
        IntStream.range(0, splitNumber.length - 1).forEach(i -> {
            int difference = Math.abs(Integer.parseInt(splitNumber[i]) - Integer.parseInt(splitNumber[i + 1]));
            if (difference != 1)
                isJumping.set(false);
        });
        return isJumping.get();
    }

    private boolean isHappy() {
        int[] ints = Stream.of(splitNumber).mapToInt(Integer::parseInt).toArray();
        int sum;
        while (true) {
            sum = Arrays.stream(ints).map(elem -> (int) Math.pow(elem, 2)).sum();
            if (String.valueOf(sum).length() == 1)
                return sum == 1;
            else
                ints = Stream.of(String.valueOf(sum).split("")).mapToInt(Integer::parseInt).toArray();
        }
    }

    public boolean propertyPresent(String[] propertiesArray, String[] propertiesToExclude) {
        boolean propertiesAvailability = Stream.of(propertiesArray)
                .filter(elem -> properties.getOrDefault(elem, false))
                .count()
                == propertiesArray.length;
        return propertiesToExclude.length != 0
                ? propertiesAvailability & Stream.of(propertiesToExclude)
                .map(elem -> elem.replace("-", ""))
                .filter(elem -> !properties.getOrDefault(elem, true)).count()
                == propertiesToExclude.length
                : propertiesAvailability;
    }

    public void print() {
        System.out.printf("\nProperties of %s\n", number);
        properties.forEach((key, value) -> System.out.printf("%s: %s\n", key, value));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        properties
                .entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .forEach(elem -> stringBuilder.append(elem.getKey()).append(", "));

        return String.format("%s is %s", new DecimalFormat("###,###").format(number), stringBuilder.toString());
    }
}

