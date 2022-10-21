package tracker.data;

import tracker.Application;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Statistics {

    private static final Pattern STATISTIC_SEPARATOR = Pattern.compile(", ");
    private final HashMap<Integer, Student> students;
    private final HashMap<String, Integer> activity;
    
    Statistics(HashMap<Integer, Student> students, HashMap<String, Integer> activity) {
        this.students = students;
        this.activity = activity;
    }

    String getSummary() {
        Map<String, int[]> coursesPoints = Map.of(
                "java", students.values().stream().mapToInt(Student::getJavaPoints).toArray(),
                "dsa", students.values().stream().mapToInt(Student::getDSAPoints).toArray(),
                "databases", students.values().stream().mapToInt(Student::getDataBasePoints).toArray(),
                "spring", students.values().stream().mapToInt(Student::getSpringPoints).toArray()
        );

        Map<String, Double> averageScore = Map.of(
                "java", (double) Arrays.stream(coursesPoints.get("java"))
                        .filter(points -> points > 0).count() / Arrays.stream(coursesPoints.get("java")).sum(),
                "dsa", (double) Arrays.stream(coursesPoints.get("dsa"))
                        .filter(points -> points > 0).count() / Arrays.stream(coursesPoints.get("dsa")).sum(),
                "databases", (double) Arrays.stream(coursesPoints.get("databases"))
                        .filter(points -> points > 0).count() / Arrays.stream(coursesPoints.get("databases")).sum(),
                "spring", (double) Arrays.stream(coursesPoints.get("spring"))
                        .filter(points -> points > 0).count() / Arrays.stream(coursesPoints.get("spring")).sum()
        );

        String most = findMostPopular(coursesPoints);
        String least = findLeastPopular(most);
        String highest = findHighestActivity();
        String lowest = findLowestActivity(highest);
        String easiest = findEasiestCourse(averageScore);
        String hardest = findHardestCourse(averageScore);

        return String.format("Most popular: %s\n" +
                        "Least popular: %s\n" +
                        "Highest activity: %s\n" +
                        "Lowest activity: %s\n" +
                        "Easiest course: %s\n" +
                        "Hardest course: %s",
                most, least, highest, lowest, easiest, hardest);
    }

    private String findMostPopular(Map<String, int[]> courses) {
        String[] array = courses.entrySet().stream()
                .filter(elem -> elem.getValue().length != 0)
                .filter(elem ->
                        Arrays.stream(elem.getValue())
                                .filter(item -> item != 0)
                                .count() == students.keySet().size()
                ).map(Map.Entry::getKey)
                .map(DataStore::courseFormat)
                .toArray(String[]::new);
        return array.length == 0 ? "n/a" : String.join(", ", array);
    }

    private String findLeastPopular(String mostPopular) {
        if (mostPopular.equals("n/a"))
            return "n/a";
        List<String> list = Arrays.stream(mostPopular.split(STATISTIC_SEPARATOR.pattern())).collect(Collectors.toList());
        String result =  String.join(", ",
                Application.COURSES.stream().map(DataStore::courseFormat)
                        .filter(elem -> !list.contains(elem)).peek(System.out::println).toArray(String[]::new)
        );
        return result.isBlank() ? "n/a" : result;
    }

    private String findHighestActivity() {
        int max = activity.values().stream()
                .filter(elem -> elem != 0)
                .max(Comparator.comparingInt(elem -> elem))
                .orElse(-1);
        String[] array = activity.entrySet().stream()
                .filter(elem -> elem.getValue() == max)
                .map(Map.Entry::getKey)
                .map(DataStore::courseFormat)
                .toArray(String[]::new);
        return array.length == 0 ? "n/a" : String.join(", ", array);
    }

    private String findLowestActivity(String highestActivity) {
        if (highestActivity.equals("n/a"))
            return "n/a";

        int min = activity.entrySet().stream()
                .filter(elem ->
                        !Arrays.stream(highestActivity.split(STATISTIC_SEPARATOR.pattern()))
                                .collect(Collectors.toList()).contains(DataStore.courseFormat(elem.getKey())))
                .filter(elem -> elem.getValue() != 0)
                .map(Map.Entry::getValue)
                .min(Comparator.comparingInt(elem -> elem))
                .orElse(-1);
        String[] array = activity.entrySet().stream()
                .filter(elem -> elem.getValue() == min)
                .map(Map.Entry::getKey)
                .map(DataStore::courseFormat)
                .toArray(String[]::new);
        return array.length == 0 ? "n/a" : String.join(", ", array);
    }

    private String findEasiestCourse(Map<String, Double> averageScore) {
        return averageScore.entrySet().stream().filter(elem -> elem.getValue() > 0.0)
                .min(Comparator.comparingDouble(Map.Entry::getValue))
                .map(elem -> DataStore.courseFormat(elem.getKey())).orElse("n/a");
    }

    private String findHardestCourse(Map<String, Double> averageScore) {
        return averageScore.entrySet().stream().filter(elem -> elem.getValue() > 0.0)
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(elem -> DataStore.courseFormat(elem.getKey())).orElse("n/a");
    }

    String getSummaryByCourse(String course) {
        Function<Double, String> round = value -> {
            DecimalFormat decimalFormat = new DecimalFormat("###0.0");
            return decimalFormat.format(
                    BigDecimal.valueOf(value).round(new MathContext(2, RoundingMode.HALF_UP))
            );
        };
        ArrayList<String> result = new ArrayList<>();
        result.add(DataStore.courseFormat(course));
        result.add("id    points    completed");
        Predicate<Map.Entry<Integer, Student>> filter = null;
        Consumer<Map.Entry<Integer, Student>> forEach = null;
        Comparator<Map.Entry<Integer, Student>> comparator = null;
        switch (course) {
            case "java": {
                filter = elem -> elem.getValue().getJavaPoints() > 0;
                comparator = (f, s) -> Integer.compare(s.getValue().getJavaPoints(), f.getValue().getJavaPoints());
                forEach = elem -> result.add(String.format(
                        "%s %s        %s%%",
                        elem.getKey(),
                        elem.getValue().getJavaPoints(),
                        round.apply((elem.getValue().getJavaPoints() / DataStore.JAVA_MAX_POINTS) * 100.0)));
                break;
            }
            case "dsa": {
                filter = elem -> elem.getValue().getDSAPoints() > 0;
                comparator = (f, s) -> Integer.compare(s.getValue().getDSAPoints(), f.getValue().getDSAPoints());
                forEach = elem -> result.add(String.format(
                        "%s %s        %s%%",
                        elem.getKey(),
                        elem.getValue().getDSAPoints(),
                        round.apply((elem.getValue().getDSAPoints() / DataStore.DSA_MAX_POINTS) * 100.0)));
                break;
            }
            case "databases": {
                filter = elem -> elem.getValue().getDataBasePoints() > 0;
                comparator = (f, s) -> Integer.compare(s.getValue().getDataBasePoints(), f.getValue().getDataBasePoints());
                forEach = elem -> result.add(String.format(
                        "%s %s        %s%%",
                        elem.getKey(),
                        elem.getValue().getDataBasePoints(),
                        round.apply((elem.getValue().getDataBasePoints() / DataStore.DATABASES_MAX_POINTS) * 100.0)));
                break;
            }
            case "spring": {
                filter = elem -> elem.getValue().getSpringPoints() > 0;
                comparator = (f, s) -> Integer.compare(s.getValue().getDataBasePoints(), f.getValue().getDataBasePoints());
                forEach = elem -> result.add(String.format(
                        "%s %s        %s%%",
                        elem.getKey(),
                        elem.getValue().getSpringPoints(),
                        round.apply((elem.getValue().getSpringPoints() / DataStore.SPRING_MAX_POINTS) * 100.0)));
                break;
            }
        }
        students.entrySet().stream().filter(filter).sorted(comparator).forEach(forEach);
        return String.join("\n", result);
    }
}
