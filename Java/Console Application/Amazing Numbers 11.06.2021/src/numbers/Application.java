package numbers;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Application {

    public void run() {
        String[] msg = {
                "Welcome to Amazing Numbers!\n\n",
                "Supported requests:\n",
                "- enter a natural number to know its properties;\n",
                "- enter two natural numbers to obtain the properties of the list:\n",
                "  * the first parameter represents a starting number;\n",
                "  * the second parameter shows how many consecutive numbers are to be processed;\n",
                "- two natural numbers and properties to search for;\n",
                "- a property preceded by minus must not be present in numbers;\n",
                "- separate the parameters with one space;\n",
                "- enter 0 to exit.\n\n",
        };
        Stream.of(msg).forEach(System.out::print);
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.print("Enter a request:");
            String[] input = inputValidation(scanner);
            if (input.length > 0) {
                if (input.length == 1)
                    single(Long.parseLong(input[0]));
                else if (input.length == 2)
                    list(Long.parseLong(input[0]), Short.parseShort(input[1]));
                else
                    showNumberByProperty(Long.parseLong(input[0]), Short.parseShort(input[1]),
                            Stream.of(input).skip(2).toArray(String[]::new));
            }

            System.out.println();
        }
    }

    private String[] inputValidation(Scanner scanner) {
        String[] properties = {"buzz", "duck", "palindromic", "gapful",
                "spy", "even", "odd", "square", "sunny", "jumping", "happy", "sad"};

        String[] input = scanner.nextLine().toLowerCase().split("\\s");
        String[] validInput = Stream.of(input).toArray(String[]::new);
        long inputNumber = Long.parseLong(input[0]);
        if (inputNumber == 0)
            System.exit(0);
        long range;
        switch (input.length) {
            case 1:
                if (inputNumber > 0)
                    return validInput;
                else
                    System.out.println("The first parameter should be a natural number or zero.");
                break;
            case 2:
                range = Long.parseLong(input[1]);
                if (inputNumber > 0 & range > 0)
                    return validInput;
                else
                    System.out.println("The second parameter should be a natural number.");
                break;
            default:
                range = Long.parseLong(input[1]);
                input = Stream.of(input).skip(2).toArray(String[]::new);
                Predicate<String> propertyValidation = property -> !((!property.matches("[\\+\\.\\*\\?]+")
                        && Stream.of(properties)
                        .filter(p -> p.matches(property.toLowerCase()))
                        .findFirst().orElse("other").matches(property.toLowerCase())));

                boolean allPropertiesValidated = Stream.of(input)
                        .map(elem -> elem.replace("-", ""))
                        .noneMatch(propertyValidation);

                if (inputNumber > 0 & range > 0 & allPropertiesValidated) {
                    if (!findingPresenceOfMutuallyExclusiveProperties(input)) {
                        return validInput;
                    }
                } else {
                    List<String> defectiveInputProperties = Stream.of(input)
                            .map(elem -> elem.replace("-", ""))
                            .filter(propertyValidation).collect(Collectors.toList());
                    StringBuilder stringBuilder = new StringBuilder(defectiveInputProperties.get(0).toUpperCase());
                    defectiveInputProperties.stream().skip(1).forEach(elem ->
                            stringBuilder.append(", ").append(elem.toUpperCase()));
                    System.out.printf("The %s [%s] %s wrong.\n" +
                                    "Available properties: " +
                                    "[EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, HAPPY, SAD]\n",
                            defectiveInputProperties.size() == 1 ? "property" : "properties",
                            stringBuilder.toString(),
                            defectiveInputProperties.size() == 1 ? "is" : "are"
                    );
                }
                break;
        }
        return new String[0];
    }

    private boolean findingPresenceOfMutuallyExclusiveProperties(String[] input) {
        AtomicBoolean mutuallyExclusiveProperties = new AtomicBoolean(true);
        Set<String> mutuallyExclusivePropertiesSet = new HashSet<>();
        Map<String, Long> countProperties =
                Stream.of(input)
                        .map(elem -> elem.replace("-", ""))
                        .collect(groupingBy(Function.identity(), counting()));
        countProperties.forEach((key, value) -> {
            if (value == 2)
                if (Stream.of(input).anyMatch(elem -> Pattern.compile("-" + key).matcher(elem).find()))
                    mutuallyExclusiveProperties.set(false);
                    Stream.of(input)
                            .filter(elem -> elem.matches("-*" + key))
                            .forEach(mutuallyExclusivePropertiesSet::add);
        });
        String[][] properties = {
                {"-odd", "-even"},
                {"odd", "even"},

                {"-duck", "-spy"},
                {"duck", "spy"},

                {"-sunny", "-square"},
                {"sunny", "square"},

                {"-sad", "-happy"},
                {"sad", "happy"},

        };
        if (mutuallyExclusiveProperties.get())
            Stream.of(input).forEach(first -> Stream.of(input).forEach(second -> {
                for (String[] elem : properties) {
                    if (first.matches(elem[0])
                            & second.matches(elem[1])) {
                        mutuallyExclusiveProperties.set(false);
                        mutuallyExclusivePropertiesSet.add(first);
                        mutuallyExclusivePropertiesSet.add(second);
                    }
                }
            }));
        if (!mutuallyExclusiveProperties.get()) {
            List<String> mutuallyExclusivePropertiesList = new ArrayList<>(mutuallyExclusivePropertiesSet);
            StringBuilder stringBuilder = new StringBuilder(mutuallyExclusivePropertiesList.get(0).toUpperCase());
            mutuallyExclusivePropertiesList
                    .stream()
                    .skip(1)
                    .forEach(elem -> stringBuilder.append(", ").append(elem.toUpperCase()));
            System.out.printf("The request contains mutually exclusive properties: [%s]\n" +
                            "There are no numbers with these properties.\n",
                    stringBuilder.toString());
            return true;
        }
        return false;
    }

    private void single(Long inputNumber) {
        Number number = new Number(inputNumber);
        number.print();
    }

    private void list(Long inputNumber, short range) {
        List<Number> numberList = new ArrayList<>();
        IntStream.range(0, range).forEach(s -> numberList.add(new Number(inputNumber + s)));
        numberList.forEach(elem -> System.out.println(elem.toString()));
    }

    private void showNumberByProperty(long inputNumber, short numberOfCount, String[] properties) {
        String[] propertiesToExclude = Stream.of(properties)
                .filter(elem -> elem.matches("(-\\D++)"))
                .toArray(String[]::new);
        String[] prop = Stream.of(properties)
                .filter(elem -> !elem.matches("(-\\D++)"))
                .toArray(String[]::new);
        Stream.iterate(0, i -> i + 1)
                .parallel()
                .map(i -> new Number(inputNumber + i))
                .filter(first -> first.propertyPresent(prop, propertiesToExclude))
                .limit(numberOfCount)
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.comparing(Number::getNumber))
                .forEach(elem -> System.out.println(elem.toString()));
    }
}

