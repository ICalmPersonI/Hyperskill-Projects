package tracker.data;

import tracker.Application;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;


public class DataStore {
    static final double JAVA_MAX_POINTS = 600;
    static final double DSA_MAX_POINTS = 400;
    static final double DATABASES_MAX_POINTS = 480;
    static final double SPRING_MAX_POINTS = 550;

    private int id = 10000;
    private final HashMap<Integer, Student> students = new LinkedHashMap<>();
    private final HashMap<String, Integer> activity = new HashMap<>() {{
        put("java", 0);
        put("dsa", 0);
        put("databases", 0);
        put("spring", 0);
    }};
    private final HashMap<Integer, ArrayList<String>> receivedCertificate = new HashMap<>();

    public boolean containEmail(String email) {
        return students.values().stream().noneMatch(elem -> elem.getEmail().equals(email));
    }

    public void addStudent(String firstName, String lastName, String email) {
        Student student = new Student(id++, firstName, lastName, email);
        students.put(student.getId(), student);
    }

    public boolean addPoints(int id, int java, int dsa, int databases, int spring) {
        if (students.containsKey(id)) {
            Student s = students.get(id);
            s.addJavaPoint(java);
            s.addDSAPoint(dsa);
            s.addDataBasePoint(databases);
            s.addSpringPoint(spring);
            updateActivity(java, dsa, databases, spring);
            return true;
        } else {
            return false;
        }
    }

    private void updateActivity(int java, int dsa, int databases, int spring) {
        if (java != 0)
            activity.put("java", activity.get("java") + 1);
        if (dsa != 0)
            activity.put("dsa", activity.get("dsa") + 1);
        if (databases != 0)
            activity.put("databases", activity.get("databases") + 1);
        if (spring != 0)
            activity.put("spring", activity.get("spring") + 1);
    }

    public String getStudents() {
        if (students.isEmpty()) {
            return "No students found.";
        } else {
            StringBuilder sb = new StringBuilder("Students:\n");
            students.values().forEach(elem -> sb.append(elem.getId()).append("\n"));
            return sb.toString();
        }
    }

    public String find(int id) {
        if (students.containsKey(id))
            return students.get(id).toString();
        else
            return String.format("No student is found for id=%s", id);

    }

    public String getSummary() {
        return new Statistics(students, activity).getSummary();
    }

    public String getSummaryByCourse(String course) {
        return new Statistics(students, activity).getSummaryByCourse(course);
    }

    public HashMap<String, ArrayList<String>> getCertificates() {
        HashMap<String, ArrayList<String>> certificates = new LinkedHashMap<>();

        Function<String[], String> createCertificate = data ->
                String.format("To: %s\n" +
                                "Re: Your Learning Progress\n" +
                                "Hello, %s! You have accomplished our %s course!",
                        data[0], data[1], data[2]);
        BiConsumer<String, String> addCertificate = (fullName, certificate) ->  {
            if (certificates.containsKey(fullName)) {
                if (!certificates.get(fullName).contains(certificate))
                    certificates.get(fullName).add(certificate);
            } else {
                certificates.put(fullName, new ArrayList<>() {{
                    add(certificate);
                }});
            }
        };

        Predicate<Map.Entry<Integer, Student>> filter = null;
        for (String course : Application.COURSES) {
            switch (course) {
                case "java": filter = elem -> elem.getValue().getJavaPoints() == JAVA_MAX_POINTS; break;
                case "dsa": filter = elem -> elem.getValue().getDSAPoints() == DSA_MAX_POINTS; break;
                case "databases": filter = elem -> elem.getValue().getDataBasePoints() == DATABASES_MAX_POINTS; break;
                case "spring": filter = elem -> elem.getValue().getSpringPoints() == SPRING_MAX_POINTS; break;
            }
            students.entrySet().stream()
                    .filter(filter)
                    .forEach(elem -> {
                        int key = elem.getKey();
                        Student s = students.get(key);
                        String fullName = s.getFullName();
                        String certificate = createCertificate.apply(
                                new String[] {s.getEmail(), fullName, courseFormat(course)}
                        );
                        if (receivedCertificate.containsKey(key)) {
                            if (!receivedCertificate.get(key).contains(course)) {
                                receivedCertificate.get(key).add(course);
                                addCertificate.accept(fullName, certificate);
                            }
                        } else {
                            addCertificate.accept(fullName, certificate);
                            receivedCertificate.put(key, new ArrayList<>() {{ add(course); }});
                        }
                    });
        }
        return certificates;
    }

    static String courseFormat(String course) {
        switch (course) {
            case "java": return "Java";
            case "dsa": return "DSA";
            case "databases": return "Databases";
            case "spring": return "Spring";
            default: return null;
        }
    }
}
