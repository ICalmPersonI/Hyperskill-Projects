package blockchain.entity;


import java.util.*;
import java.util.stream.Stream;

public class Blockchain {
    private final List<Block> blockList = new ArrayList<>();
    private String complexity = "";
    private List<Data> messages = new ArrayList<>();


    public void addMassage(Data message) {
        messages.add(message);

    }

    public void addNewBock(Block block, Miner miner) {
        if (blockList.size() == 0) {
            block.setData("No transactions");
            blockList.add(block);
            //increaseComplexity();
        } else {

            if (block.getPrevHash().compareTo(blockList.get(blockList.size() - 1).getHash()) == 0
                    && block.getHash().startsWith(complexity)) {
                StringBuffer sb = new StringBuffer();
                messages.forEach(elem -> sb.append(elem.toString()));
                messages = new ArrayList<>();
                //sb.setLength(sb.length() - 1);
                block.setData("miner9 sent 30 VC to miner1\n" +
                        "miner9 sent 30 VC to miner2\n" +
                        "miner9 sent 30 VC to Nick");
                blockList.add(block);
                miner.addCount(100);
                //increaseComplexity();
            }
        }

    }
    
    public String getHashPrevBlock() {
        if (blockList.isEmpty()) {
            return "0";
        } else {
            return blockList.get(blockList.size() - 1).getHash();
        }
    }

    public int createNewId() {
        if (blockList.isEmpty()) {
            return 1;
        } else {
            return blockList.get(blockList.size() - 1).getId() + 1;
        }
    }
    
    private void increaseComplexity() {
        if (complexity.isEmpty()) {
            complexity = "0";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            Stream.iterate(0, i -> i <= complexity.length(), i -> i + 1).forEach(i -> stringBuilder.append("0"));
            complexity = stringBuilder.toString();
        }
    }

    public int getSize() {
        return blockList.size();
    }

    public String getComplexity() {
        return complexity;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        blockList.stream().limit(15).forEach(elem -> stringBuffer.append(elem.toString()));
        return stringBuffer.toString();
    }
}
