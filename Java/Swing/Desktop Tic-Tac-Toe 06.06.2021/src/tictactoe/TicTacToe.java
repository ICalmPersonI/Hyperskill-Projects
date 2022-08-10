package tictactoe;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class TicTacToe extends JFrame {
    private final GameField gameField = new GameField();

    private final View view = new View();

    private final Controller controller = new Controller(gameField, view);

    private final JMenuBar menuBar = menuBar();
    private final JLabel gameStatusPanel = controller.getGameStatus();
    private final JButton firstPlayer = button("ButtonPlayer1");
    private final JButton secondPlayer = button("ButtonPlayer2");
    private final JButton startReset = startReset();
    private final JPanel menu = menu();

    private boolean preset = false;



    public TicTacToe() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setTitle("Tic Tac Toe");
        setLayout(new BorderLayout());
        view.initListeners(controller);
        setJMenuBar(menuBar);
        getContentPane().add(menu, BorderLayout.PAGE_START);
        getContentPane().add(gameStatusPanel, BorderLayout.PAGE_END);
        getContentPane().add(view, BorderLayout.CENTER);
        setVisible(true);
    }

    private JMenuBar menuBar() {
        InitPlayer initPlayer = (mode, first, second, name) -> {
            JMenuItem menuItem = new JMenuItem(mode);
            menuItem.setName(name);
            menuItem.addActionListener(event -> {
                preset = true;
                if (startReset.getText().matches("Start")) {
                    start();
                    startReset.setText("Reset");
                } else {
                    reset();
                    start();
                }
                firstPlayer.setEnabled(false);
                secondPlayer.setEnabled(false);
                firstPlayer.setText(first ? "Robot" : "Human");
                secondPlayer.setText(second ? "Robot" : "Human");
                controller.setFirstPlayer(new Player("X", first));
                controller.setSecondPlayer(new Player("O", second));
                controller.firstMove();
            });
            return menuItem;
        };

        List<JMenuItem> gameModes = List.of(
                initPlayer.init("Human vs Human", false, false, "MenuHumanHuman"),
                initPlayer.init("Human vs Robot", false, true, "MenuHumanRobot"),
                initPlayer.init("Robot vs Human", true, false, "MenuRobotHuman"),
                initPlayer.init("Robot vs Robot", true, true, "MenuRobotRobot")
        );

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setName("MenuGame");

        gameModes.forEach(gameMenu::add);
        gameMenu.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        exit.setName("MenuExit");
        exit.addActionListener(event -> System.exit(0));
        gameMenu.add(exit);

        menuBar.add(gameMenu);
        return menuBar;
    }

    private JPanel menu() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 3));
        panel.add(firstPlayer);
        panel.add(startReset);
        panel.add(secondPlayer);
        return panel;
    }

    private JButton button(String name) {
        JButton button = new JButton("Human");
        button.setName(name);
        button.addActionListener(event -> button.setText(button.getText().matches("Human") ? "Robot" : "Human"));
        return button;
    }

    private JButton startReset() {
        JButton button = new JButton("Start");
        button.setName("ButtonStartReset");
        button.addActionListener(event -> {
            if (button.getText().matches("Start")) {
                firstPlayer.setEnabled(false);
                secondPlayer.setEnabled(false);
                start();

                controller.setSecondPlayer(
                        secondPlayer.getText().matches("Robot")
                                ? new Player("O", true)
                                : new Player("O", false)
                );

                controller.setFirstPlayer(
                        firstPlayer.getText().matches("Robot")
                                ? new Player("X", true)
                                : new Player("X", false)
                );

                button.setText("Reset");
                controller.firstMove();
            } else {
                firstPlayer.setEnabled(true);
                secondPlayer.setEnabled(true);
                reset();
                button.setText("Start");
            }
        });
        return button;
    }

    private void start() {
        view.start();
        controller.start();
        menuBar.setEnabled(false);
    }

    private void reset() {
        view.reset();
        controller.reset();
        controller.setGameStatus("Game is not started");
    }
}

interface InitPlayer {
    JMenuItem init(String mode, boolean first, boolean second, String name);
}