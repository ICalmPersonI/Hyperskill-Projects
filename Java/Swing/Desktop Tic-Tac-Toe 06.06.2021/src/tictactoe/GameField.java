package tictactoe;

import java.util.Arrays;

public class GameField {
    private final String[][] field;
    private String gameStatus;

    GameField() {
        this.field = new String[][] {
                {"", "", ""},
                {"", "", ""},
                {"", "", ""}
        };
        this.gameStatus = "Game not started";
    }

    public void put(int row, int col, String letter) {
        this.field[row][col] = letter;
        this.gameStatus = verdict();

    }

    public String[][] getField() {
        return field;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    private String verdict() {
        if (Arrays.stream(field)
                .flatMap(array ->
                        Arrays.stream(array)
                                .filter(elem -> elem.matches("X") || elem.matches("O"))
                ).count() == 9) {
            return gameStatus = "Draw";
        }

        String[] letter = {"X", "O"};
        for (String value : letter) {
            for (int s = 0; s < field.length; s++) {
                if (field[s][0].matches(value) & field[s][1].matches(value) & field[s][2].matches(value)) {
                    return gameStatus = String.format("(%s) wins", value);
                }
                if (field[0][s].matches(value) & field[1][s].matches(value) & field[2][s].matches(value)) {
                    return gameStatus = String.format("(%s) wins", value);
                }
                if (field[0][0].matches(value) & field[1][1].matches(value) & field[2][2].matches(value)) {
                    return gameStatus = String.format("(%s) wins", value);
                }
                if (field[0][2].matches(value) & field[1][1].matches(value) & field[2][0].matches(value)) {
                    return gameStatus = String.format("(%s) wins", value);
                }
            }
        }

        return gameStatus = "Game in progress";
    }
}
