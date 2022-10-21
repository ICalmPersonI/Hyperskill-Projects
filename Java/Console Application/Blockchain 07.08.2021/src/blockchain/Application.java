package blockchain;

import blockchain.entity.Block;
import blockchain.entity.Blockchain;
import blockchain.entity.Data;
import blockchain.entity.Miner;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {


    public void run() {
        Blockchain blockchain = new Blockchain();

        Thread thread = new Thread("Talking") {
            final List<Data> messages = List.of(
                    new Data("miner1", 30, "miner18"),
                    new Data("miner2", 40, "miner17"),
                    new Data("miner3", 50, "miner16"),
                    new Data("miner4", 60, "miner15"),
                    new Data("miner5", 70, "miner14"),
                    new Data("miner6", 80, "miner13"),
                    new Data("miner7", 90, "miner12"),
                    new Data("miner8", 100, "miner11"),
                    new Data("miner9", 110, "miner10"),
                    new Data("miner10", 120, "miner9"),
                    new Data("miner11", 130, "miner8"),
                    new Data("miner12", 140, "miner7"),
                    new Data("miner13", 150, "miner6"),
                    new Data("miner14", 160, "miner5"),
                    new Data("miner15", 170, "miner4"),
                    new Data("miner16", 180, "miner3"),
                    new Data("miner17", 190, "miner2"),
                    new Data("miner17", 200, "miner1")


            );

            @Override
            public void run() {
                Random random = new Random();
                while (true) {
                    try {
                        sleep(30);
                        blockchain.addMassage(messages.get(random.nextInt(messages.size() - 1)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();

        ExecutorService executorService = Executors.newFixedThreadPool(1000);

        for (int i = 1; i < 16; i++) {
            int finalI = i;

            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


                long timeStamp = new Date().getTime();

                String prevHash = blockchain.getHashPrevBlock();
                blockchain.addNewBock(
                        new Block(String.format("miner%d", finalI), blockchain.createNewId(), timeStamp, prevHash, blockchain.getComplexity()),
                        new Miner(String.format("miner%d", finalI))
                );


        }
       //executorService.shutdownNow();
        //thread.interrupt();

        while (true) {

            if (blockchain.getSize() > 14) {
                System.out.println(blockchain);
                System.exit(0);
                break;
            }
        }

    }
}
