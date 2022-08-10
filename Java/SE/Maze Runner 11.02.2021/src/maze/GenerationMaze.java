package maze;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.Scanner;

class GenerationMaze {
    private int[][] maze;

     void generation() {
        System.out.println("Please, enter the size of a maze");
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();
        maze = new int[size][size];
        generateMaze();
    }

    private void generateMaze() {
        for (int i = 0; i < maze.length; i = i + 2) {
            for (int s = 1; s < maze[i].length; s++) {
                maze[i][s] = 1;
            }
        }
        for (int i = 0; i < maze.length; i++) {
            for (int s = 0; s < maze[i].length; s = s + 2) {
                maze[i][s] = 1;
            }
        }

        stackHor.add(hor);
        stackVer.add(ver);

        generate();

        for (int i = 1; i < maze.length; i++) {
            for (int s = 1; s < maze[i].length; s++) {
                if (maze[i][s] == 0) {
                    if (s + 1 < maze[maze.length - 1].length - 1) {
                        if (maze[i][s - 1] == 1 && maze[i][s + 1] == 1) {
                            maze[i][s - 1] = 0;
                            maze[i][s + 1] = 0;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < maze.length; i++) {
            maze[i][0] = 1;
            maze[i][maze[maze.length - 1].length - 1] = 1;
        }

        for (int i = 0; i < maze[maze.length - 1].length; i++) {
            maze[0][i] = 1;
            maze[maze.length - 1][i] = 1;
        }
        maze[0][1] = 0;
        maze[maze.length - 1][maze[maze.length - 1].length - 2] = 0;
        maze[maze.length - 2][maze[maze.length - 1].length - 2] = 0;
        maze[maze.length - 3][maze[maze.length - 1].length - 2] = 0;
    }

    private int ver = 1;
    private int hor = 1;
    private final Deque<Integer> stackVer = new ArrayDeque<>();
    private final Deque<Integer> stackHor = new ArrayDeque<>();
    private boolean impasseUp = false;
    private boolean impasseLeft = false;
    private boolean impasseRight = false;
    private boolean impasseDown = false;

    private int generate() {
        int dir = new Random().nextInt(4 - 1 + 1) + 1;
        direction(dir);
        if (stackVer.size() < 1 || stackHor.size() < 1) {
            return 1;
        }
        if (impasseUp && impasseLeft && impasseRight && impasseDown) {
            hor = stackHor.pollFirst();
            ver = stackVer.pollFirst();
        }
        return generate();
    }

    private void direction(int dirSelect) {

        if (dirSelect == 1) {
            if (hor - 2 > 0) up();
            else impasseUp = true;
        }
        if (dirSelect == 2) {
            if (ver - 2 > 0) left();
            else impasseLeft = true;
        }
        if (dirSelect == 3) {
            if (ver + 2 < maze[maze.length - 1].length - 1) right();
            else impasseRight = true;
        }
        if (dirSelect == 4) {
            if (hor + 2 < maze.length - 1) down();
            else impasseDown = true;
        }

    }

    private void up() {
        if (maze[hor - 2][ver] == 0) {
            maze[hor][ver] = 3;
            maze[hor - 1][ver] = 3;
            stackVer.add(ver);
            stackHor.add(hor);
            stackVer.add(ver);
            stackHor.add(hor - 1);
            hor = hor - 2;
            impasseUp = false;
        } else if (maze[hor - 1][ver] == 0) {
            maze[hor][ver] = 3;
            stackVer.add(ver);
            stackHor.add(hor);
            hor = hor - 1;
            impasseUp = false;
        } else impasseUp = true;
    }

    private  void left() {
        if (maze[hor][ver - 2] == 0) {
            maze[hor][ver] = 3;
            maze[hor][ver - 1] = 3;
            stackVer.add(ver);
            stackHor.add(hor);
            stackVer.add(ver - 1);
            stackHor.add(hor);
            ver = ver - 2;
            impasseLeft = false;
        } else impasseLeft = true;
    }

    private  void right() {
        if (maze[hor][ver + 2] == 0) {
            maze[hor][ver] = 3;
            maze[hor][ver + 1] = 3;
            stackVer.add(ver);
            stackHor.add(hor);
            stackVer.add(ver + 1);
            stackHor.add(hor);
            ver = ver + 2;
            impasseRight = false;
        } else impasseRight = true;
    }

    private  void down() {
        if (maze[hor + 2][ver] == 0) {
            maze[hor][ver] = 3;
            maze[hor + 1][ver] = 3;
            stackVer.add(ver);
            stackHor.add(hor);
            stackVer.add(ver);
            stackHor.add(hor + 1);
            hor = hor + 2;
            impasseDown = false;
        } else impasseDown = true;
    }

    int[][] getMaze() {
        for (int i = 0; i < maze.length; i++) {
            for (int s = 0; s < maze[i].length; s++) {
                if (maze[i][s] == 3) maze[i][s] = 0;
            }
        }
        return maze;
    }

}

