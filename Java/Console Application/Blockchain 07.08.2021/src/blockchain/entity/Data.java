package blockchain.entity;

public class Data {
    private final String name;
    private final int count;
    private final String from;

    public Data(String name, int count, String from) {
        this.name = name;
        this.count = count;
        this.from = from;
    }

    @Override
    public String toString() {
        return String.format("%s sent %d VC to %s\n", name, count, from);
    }
}
