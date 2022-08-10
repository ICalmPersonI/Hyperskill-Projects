package blockchain.entity;

public class Miner {
    private final String name;
    private int count = 100;

    public Miner(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void addCount(int count) {
        this.count += count;
    }
}
