package maze;

/*
I did not master it.
I took from here
https://www.codesdope.com/blog/article/backtracking-to-solve-a-rat-in-a-maze-c-java-pytho/
and adapted it myself.
 */
public class Runner {

    static int[][] maze;
    private static int[][] solution;
    private static int SIZE;

    Runner(int[][] maze) {
        Runner.maze = maze;
        solution = maze;
        SIZE = maze.length;
    }
    //
    void findEscape() {
        solveMaze(0,1);
        mazeView();
    }

    private static boolean solveMaze(int ver, int hor) {

        if((ver == SIZE-1) && (hor == SIZE-2)) {
            solution[ver][hor] = 3;
            return true;
        }

        if(ver >= 0 && hor >= 0 && ver < SIZE && hor < SIZE && solution[ver][hor] == 0 && maze[ver][hor] == 0) {
            solution[ver][hor] = 3;
            if(solveMaze(ver + 1, hor))
                return true;
            if(solveMaze(ver, hor + 1))
                return true;
            if(solveMaze(ver - 1, hor))
                return true;
            if(solveMaze(ver, hor - 1))
                return true;
            solution[ver][hor] = 0;
            return false;
        }
        return false;

    }

    private static void mazeView() {
        for (int[] i : solution) {
            for (int s : i) {
                if (s == 1) System.out.print("\u2588\u2588");
                else if (s == 3) System.out.print("//");
                else System.out.print("  ");
            }
            System.out.println();
        }
    }
}
