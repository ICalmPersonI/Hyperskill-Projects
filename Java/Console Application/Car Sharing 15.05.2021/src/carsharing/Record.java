package carsharing;

public class Record {
    private final int id;
    private final String name;
    private final int fk_id;

    Record(int id, String name, int fk_id) {
        this.id = id;
        this.name = name;
        this.fk_id = fk_id;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFk_id() {
        return fk_id;
    }
}
