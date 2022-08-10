package tictactoe;

public class Player {
    private final String letter;
    private final boolean isBot;

    Player(String letter, boolean isBot) {
        this.letter = letter;
        this.isBot = isBot;
    }

    public String getLetter() {
        return this.letter;
    }

    public boolean isBot() {
        return this.isBot;
    }
}
