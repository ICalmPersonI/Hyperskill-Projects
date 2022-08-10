package tictactoe;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Controller {
    private GameField gameField;
    private final View view;
    private final JLabel gameStatus = new JLabel();
    private Player firstPlayer;
    private Player secondPlayer;

    Controller(GameField gameField, View view) {
        this.gameField = gameField;
        this.view = view;
        gameStatus.setText("Game is not started");
        gameStatus.setName("LabelStatus");
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void firstMove() {
        if (firstPlayer.isBot()) {
            botMove();
        }
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void put(int col, int row, String letter) {
        gameField.put(col, row, letter);
        update();
    }

    public void reset() {
        this.gameField = new GameField();
        this.gameStatus.setText("Game is not started");
    }

    public void start() {
        String text;
        try {
            text = String.format("The turn of %s Player (%s)", "Human", "X");
        } catch (NullPointerException npe) {
            text = String.format("The turn of %s Player (%s)", "Robot", "X");
        }
        this.gameStatus.setText(text);
    }

    public JLabel getGameStatus() {
        return this.gameStatus;
    }

    public void setGameStatus(String text) {
        this.gameStatus.setText(text);
    }

    public void botMove() {
        List<String> freeCell = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> IntStream.range(0, 3).forEach(s -> {
            if (gameField.getField()[i][s].matches(""))
                freeCell.add(String.format("%s %s", i, s));
        }));

        if (!freeCell.isEmpty()) {
            String[] coord = freeCell.get((int) (Math.random() * freeCell.size())).split("\\s");

            int row = Integer.parseInt(coord[0]);
            int col = Integer.parseInt(coord[1]);

            ((View.Button) Arrays.stream(view.getComponents())
                    .filter(component -> ((View.Button) component).getRow() == row
                            & ((View.Button) component).getCol() == col)
                    .findFirst()
                    .orElseThrow())
                    .doClick();

        }
    }

    public void update() {
        view.update(gameField.getField());
        this.gameStatus.setText(gameField.getGameStatus());
    }
}
