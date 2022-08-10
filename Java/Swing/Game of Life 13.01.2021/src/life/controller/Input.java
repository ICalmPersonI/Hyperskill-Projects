package life.controller;

import life.View.GameOfLife;
import life.model.Universe;

import java.io.IOException;


public class Input {
    private static int sizeUniverse;

    public static void executeInput() throws IOException {
        checkInput();
    }

    private static void input() throws IOException {
        sizeUniverse = 80;
    }

    private static void checkInput() throws IOException {
        Universe universe = new Universe();
        do {
            input();
        } while (sizeUniverse < 0);
        GameOfLife gameOfLifeFrame = new GameOfLife();
        universe.setSizeUniverse(sizeUniverse);
        gameOfLifeFrame.setSizeUniverse(sizeUniverse);
    }
}
