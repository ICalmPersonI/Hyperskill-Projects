package life.View;


import life.Main;
import life.controller.Input;
import life.model.Universe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


public class GameOfLife extends JFrame {
    public GameOfLife() {

    }
    private static int sizeUniverse = 100;
    private  final ArrayList<Integer> alive = new ArrayList<>();
    private  final ArrayList<Integer> generation = new ArrayList<>();
    private  final String[][][] universe = new String[20][80][80];

    private final ImageIcon aliveCellImage = new ImageIcon("F:\\black.jpg");
    private final ImageIcon deathCellImage = new ImageIcon("F:\\white.jpg");
    private final JFrame frame = new JFrame("Game Of Life");
    private final JPanel statistic = new JPanel();
    private final JPanel grid = new JPanel();

    public void setSizeUniverse(int sizeUniverse) {
        GameOfLife.sizeUniverse = sizeUniverse;
    }

    public void setUniverse (String[][] universe, int stage) {

        for (int i = 0; i < universe.length; i++) {
            for (int s = 0 ; s < universe[i].length; s++) {
                this.universe[stage][i][s] = universe[i][s];
            }
        }

    }

    public void setAlive(int alive) {
        this.alive.add(alive);
    }

    public void setGeneration(int generation) {
        this.generation.add(generation);
    }

    public void buildFrame() throws InterruptedException {
        GameOfLife();
    }

    boolean end;
    private int count = 0;
    private void GameOfLife() throws InterruptedException {
        grid.setName("GenerationLabel");
        statistic.setName("AliveLabel");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        statistic.setLayout(new BoxLayout(statistic, BoxLayout.Y_AXIS));
        GridLayout universeField = new GridLayout(50, 50);
        grid.setLayout(universeField);
        JButton button = new JButton("Reset");
        button.setName("ResetButton");
        ActionListener listenerReset = ActionEvent -> {
            frame.setVisible(false);
            frame.dispose();
            end = true;
         Thread threadReturn = new Thread() {
             @Override
             public void run() {
                 String[] arg = {"-ret"};
                 try {
                     Main.main(arg);
                 } catch (IOException | InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         };
         Main main = new Main();
         main.mainThredClose();
         threadReturn.start();
        };
        button.addActionListener(listenerReset);

        AtomicBoolean isActive = new AtomicBoolean(true);
        JToggleButton toggleButton = new JToggleButton("Pause");
        toggleButton.setName("PlayToggleButton");
        ItemListener itemListener = itemEvent -> {
            int state = itemEvent.getStateChange();
            Thread threadPause = new Thread() {
                @Override
                public void run() {
                    int getCount = count-1;
                    while (isActive.get()) {
                        count = getCount;
                    }
                }
            };
            if (state == ItemEvent.SELECTED) {
                isActive.set(true);
                threadPause.start();

            }
            else {
                isActive.set(false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        toggleButton.addItemListener(itemListener);

        statistic.add(button);
        statistic.add(toggleButton);
        frame.add(statistic, BorderLayout.WEST);
        dynamicComponents();

    }
    void dynamicComponents() throws InterruptedException {
        while (count != generation.size()-1) {
            if (end) {
                break;
            }
            JLabel generationLabel = new JLabel("Generation #" + generation.get(count));
            JLabel aliveLabel = new JLabel("Alive: " + alive.get(count));
            statistic.add(generationLabel);
            statistic.add(aliveLabel);
            frame.add(statistic, BorderLayout.WEST);

            for (String[] line : universe[count]) {
                for (String column : line) {
                    if (column.matches("O")) {
                        grid.add(new JLabel(aliveCellImage));
                    } else {
                        grid.add(new JLabel(deathCellImage));
                    }
                }
            }

            frame.add(grid, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
            count++;
            Thread.sleep(1000);
            statistic.remove(generationLabel);
            statistic.remove(aliveLabel);
            grid.removeAll();
        }
    }
}
