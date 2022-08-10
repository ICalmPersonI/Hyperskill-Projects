package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class View extends JPanel {

    private String currentMove = "O";

    View() {
        setLayout(field());
    }

    public String getMove(String letter) {
        Map<String, String> swapMove = Map.of(
                "X", "O",
                "O", "X"
        );
        return swapMove.get(letter);
    }

    private GridLayout field() {
        GridLayout gridLayout = new GridLayout(3, 3);
        String[] letters = {"A", "B", "C"};
        IntStream.range(0, 3).forEach(i -> IntStream.range(0, 3).forEach(s -> {
            JButton button = new View.Button(i, s);
            button.setName(String.format("Button%s%s", letters[s], gridLayout.getColumns() - i));
            add(button);
        }));
        return gridLayout;
    }

    public void initListeners(Controller controller) {
        Predicate<Controller> verdict = cont -> (cont.getGameStatus().getText().matches("\\(X\\) wins")
                | cont.getGameStatus().getText().matches("\\(O\\) wins")
                | cont.getGameStatus().getText().matches("Draw"));

        Stream.of(getComponents()).forEach(component -> ((JButton) component).addActionListener(event -> {


                    int row = ((Button) component).getRow();
                    int col = ((Button) component).getCol();
                    this.currentMove = getMove(this.currentMove);
                    Player first = controller.getFirstPlayer();
                    Player second = controller.getSecondPlayer();

                    controller.put(row, col, currentMove);
                    String nextMove = getMove(this.currentMove);

                    if (verdict.test(controller)) {
                        stop();
                        String player = "";
                        if (nextMove.matches("X")) {
                            player = second.isBot() ? "Robot" : "Human";
                        } else if (nextMove.matches("O")) {
                            player = first.isBot() ? "Robot" : "Human";
                        }
                        System.out.println(player);

                        if (!controller.getGameStatus().getText().matches("Draw"))
                            controller.setGameStatus(String.format("The %s Player %s", player,
                                    controller.getGameStatus().getText()));

                    } else {

                        if (first.getLetter().matches(nextMove)) {
                            if (first.isBot()) {
                                controller.botMove();
                                controller.setGameStatus(String.format("The turn of %s Player (%s)", "Robot", nextMove));
                                this.currentMove = nextMove;
                            }
                            controller.setGameStatus(String.format("The turn of %s Player (%s)", "Human", nextMove));
                        } else if (second.getLetter().matches(nextMove)) {
                            if (second.isBot()) {
                                controller.botMove();
                                controller.setGameStatus(String.format("The turn of %s Player (%s)", "Robot", nextMove));
                                this.currentMove = nextMove;
                            }
                            controller.setGameStatus(String.format("The turn of %s Player (%s)", "Human", nextMove));
                        }
                    }

                })
        );
    }

    public void update(String[][] field) {
        Component[] components = getComponents();
        String[] strings = Stream.of(field).flatMap(Stream::of).toArray(String[]::new);
        IntStream.range(0, 9).forEach(i -> ((JButton) components[i]).setText(strings[i]));
    }

    public void stop() {
        Stream.of(getComponents()).forEach(component -> component.setEnabled(false));
    }

    public void start() {
        Stream.of(getComponents()).forEach(component -> component.setEnabled(true));
    }

    public void reset() {
        Stream.of(getComponents()).forEach(component -> ((JButton) component).setText(" "));
        stop();
    }

    static class Button extends JButton {
        int row;
        int col;

        Button(int row, int col) {
            this.row = row;
            this.col = col;
            setFont(new Font("Courier", Font.BOLD, 42));
            setText(" ");
            setEnabled(false);
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }
}
