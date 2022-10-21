package tracker.data;

class Student {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private int javaPoints = 0;
    private int DSAPoints = 0;
    private int dataBasePoints = 0;
    private int springPoints = 0;

    public Student(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addJavaPoint(int point) {
        this.javaPoints += point;
    }

    public void addDSAPoint(int point) {
        this.DSAPoints += point;
    }

    public void addDataBasePoint(int point) {
        this.dataBasePoints += point;
    }

    public void addSpringPoint(int point) {
        this.springPoints += point;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getJavaPoints() {
        return javaPoints;
    }

    public int getDSAPoints() {
        return DSAPoints;
    }

    public int getDataBasePoints() {
        return dataBasePoints;
    }

    public int getSpringPoints() {
        return springPoints;
    }

    @Override
    public String toString() {
        return String.format("%d points: Java=%d; DSA=%d; Databases=%d; Spring=%d",
                id, javaPoints, DSAPoints, dataBasePoints, springPoints);
    }

}
