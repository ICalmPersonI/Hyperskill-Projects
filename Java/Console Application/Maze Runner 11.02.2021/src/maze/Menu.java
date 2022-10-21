package maze;


import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {

    private static int[][] maze;

    static void mainMenu() {
        boolean extTheLoop = false;
        while (!extTheLoop) {
            System.out.println("\n=== Menu ===\n" +
                    "1. Generate a new maze\n" +
                    "2. Load a maze\n" +
                    "0. Exit");
            int select = new Scanner(System.in).nextInt();
            switch (select) {
                case 1:
                    GenerationMaze generationMaze = new GenerationMaze();
                    generationMaze.generation();
                    maze = generationMaze.getMaze();
                    mazeView();
                    extTheLoop = true;

                    break;
                case 2:
                    String file = "";
                    Scanner scanner = new Scanner(System.in);
                    file = scanner.nextLine();
                    loadMaze(file);
                    extTheLoop = true;
                    break;
                case 0:
                    System.out.println("Bye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Incorrect option. Please try again");
            }
        }
        new Menu().secondMenu();
    }

    private void secondMenu() {

        while (true) {
            System.out.println("\n=== Menu ===\n" +
                    "1. Generate a new maze\n" +
                    "2. Load a maze\n" +
                    "3. Save the maze\n" +
                    "4. Display the maze\n" +
                    "5. Find the escape\n" +
                    "0. Exit");
            int select = new Scanner(System.in).nextInt();
            switch (select) {
                case 1:
                    GenerationMaze generationMaze = new GenerationMaze();
                    generationMaze.generation();
                    maze = generationMaze.getMaze();
                    mazeView();
                    break;
                case 2:
                    String fileRead = "";
                    Scanner scannerRead = new Scanner(System.in);
                    fileRead = scannerRead.nextLine();
                    loadMaze(fileRead);
                    break;
                case 3:
                    String fileWrite = "";
                    Scanner scannerWrite = new Scanner(System.in);
                    fileWrite = scannerWrite.nextLine();
                    saveMaze(fileWrite);
                    break;
                case 4:
                    mazeView();
                    break;
                case 5:
                    Runner runner = new Runner(maze);
                    runner.findEscape();
                    break;
                case 0:
                    System.out.println("Bye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Incorrect option. Please try again");
            }

        }
    }

    private static void loadMaze(String fileName) {
        try {
            File file = new File("F:\\" + fileName);
            if (!file.isFile()) {
                System.out.println("The file " + fileName + " does not exist");
                return;
            }

            Scanner scannerFile = new Scanner(file);
            int lineCount = 0;
            int columnCount = 0;
            while (scannerFile.hasNextLine()) {
                lineCount++;
                String[] array = scannerFile.nextLine().split("");
                columnCount = array.length;
            }
            scannerFile.close();

            maze = new int[lineCount][columnCount];

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> lines = new ArrayList<>();
            while (bufferedReader.ready()) {
                lines.add(bufferedReader.readLine());
            }
            for (int i = 0; i < maze.length; i++) {
                for (int s = 0; s < maze[i].length; s++) {
                    String[] splitLine = lines.get(i).split("");
                    maze[i][s] = Integer.parseInt(splitLine[s]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void saveMaze(String fileName) {
        try {
            File file = new File("F:\\" + fileName);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int[] ints : maze) {
                for (int anInt : ints) {
                    bufferedWriter.write(String.valueOf(anInt));
                }
                bufferedWriter.write("\n");
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mazeView() {
        for (int[] i : maze) {
            for (int s : i) {
                if (s == 1) System.out.print("\u2588\u2588");
                else System.out.print("  ");
            }
            System.out.println();
        }
    }
}
