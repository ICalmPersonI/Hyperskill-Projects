package blockchain.entity;

import blockchain.StringUtils;

import java.util.Random;

public class Block {

    private final String creator;
    private final int id;
    private final long timestamp;
    private long magicNumber;
    private final String prevHash;
    private String hash;
    private long time;
    private final String complexity;
    private String data;

    public Block(String creator, int id, long timestamp, String prevHash, String complexity) {
        this.creator = creator;
        this.id = id;
        this.timestamp = timestamp;
        this.prevHash = prevHash;
        this.complexity = complexity;
        this.hash = generateHash();
    }

    private String generateHash() {
        long start = System.currentTimeMillis();

        String hash = "";
        if (complexity.isEmpty()) {
            magicNumber = generateMagicNumber();
            hash = StringUtils.applySha256(toString());
        } else {
            while (!hash.startsWith(complexity)) {
                magicNumber = generateMagicNumber();
                hash = StringUtils.applySha256(toString());
            }
        }
        this.hash = hash;

        long end = System.currentTimeMillis();

        time = (end - start) / 1000000000;

        return hash;
    }

    private int generateMagicNumber() {
        return new Random().nextInt(1000000000);
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    @Override
    public String toString() {
        return
                "Block:\n" +
                        "Created by: " + creator + "\n" +
                        creator + " gets 100 VC" + "\n" +
                        "Id: " + id + "\n" +
                        "Timestamp: " + timestamp + "\n" +
                        "Magic number: " + magicNumber + "\n" +
                        "Hash of the previous block: \n" +
                        prevHash + "\n" +
                        "Hash of the block: \n" +
                        hash + "\n" +
                        "Block data:\n" + data + "\n" +
                        "Block was generating for " + time + " seconds\n" +
                        "N was increased to " + (complexity.length() + 1) + "\n\n";
    }
}
