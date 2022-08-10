package life.model;


import life.View.GameOfLife;

import java.io.IOException;
import java.util.Random;


public class Universe {
    static String[][] universe;
    public static int numberOfGenerations = 20;
    int generationCount = 0;

    public void setSizeUniverse(int size) {
        Universe.universe = new String[size][size];
    }


    public void executeMethodsUniverse() throws InterruptedException, IOException {
        CurrentGeneration.populatingTheUniverse();
        GameOfLife gameOfLifeFrame = new GameOfLife();
        int count = 0;
        while (count != numberOfGenerations) {
            NextGeneration.fillNextGenerationUniverse();
            NextGeneration.cellLife();
            gameOfLifeFrame.setUniverse(universe, generationCount);
            generationCount++;
            gameOfLifeFrame.setGeneration(generationCount);
            gameOfLifeFrame.setAlive(Universe.countAlive());
            count++;
        }
        try {
            gameOfLifeFrame.buildFrame();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    static int countAlive() {
        int count = 0;
        for (int i = 0; i < universe.length; i++) {
            for (int s = 0; s < universe[i].length; s++) {
                if (universe[i][s].matches("O")) count++;
            }
        }
        return count;
    }
}

class CurrentGeneration extends Universe {

    static void populatingTheUniverse() {
        Random random = new Random(100);
        for (int i = 0; i < universe.length; i++) {
            for (int s = 0; s < universe[i].length; s++) {
                boolean value = random.nextBoolean();
                universe[i][s] = value ? "O" : " ";

            }
        }
    }

}

class NextGeneration extends Universe {

    private static String[][] nextGenerationUniverse = new String[universe.length][universe.length];

    public static void fillNextGenerationUniverse() {
        for (int i = 0; i < nextGenerationUniverse.length; i++) {
            for (int s = 0; s < nextGenerationUniverse[i].length; s++) {
                nextGenerationUniverse[i][s] = " ";
            }
        }
    }

    static void cellLife() {
        for (int i = 0; i < nextGenerationUniverse.length; i++) {
            for (int s = 0; s < nextGenerationUniverse[i].length; s++) {
                countLiveNeighbors(i, s);
            }
        }

        for (int i = 0; i < nextGenerationUniverse.length; i++) {
            for (int s = 0; s < nextGenerationUniverse[i].length; s++) {
                universe[i][s] = nextGenerationUniverse[i][s];
            }
        }
    }

    private static void countLiveNeighbors(int x, int y) {
        int countLive = 0;
        for (int i = x - 1, s = y - 1, count = 0; count < 8; count++) {
            if (i < 0) i = nextGenerationUniverse.length - 1;
            if (s < 0) s = nextGenerationUniverse.length - 1;
            if (universe[i][s].matches("O")) countLive++;
            if (count > 5) {
                i--;
            } else if (count > 3) {
                s--;
                if (s < 0) s = nextGenerationUniverse.length - 1;
            } else if (count > 1) {
                i++;
                if (i > nextGenerationUniverse.length - 1) i = 0;
            } else {
                s++;
                if (s > nextGenerationUniverse.length - 1) s = 0;
            }
        }
        if (universe[x][y].matches("O")) {
            if (countLive == 2 || countLive == 3) {
                nextGenerationUniverse[x][y] = "O";
            } else {
                nextGenerationUniverse[x][y] = " ";
            }
        } else if (universe[x][y].matches(" ")) {
            if (countLive == 3) {
                nextGenerationUniverse[x][y] = "O";
            }
        }
    }

}

