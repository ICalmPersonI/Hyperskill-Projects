package sorting;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public interface Sorting {
    Scanner scanner = new Scanner(System.in);

    void input(String inpytFile) throws IOException;

    void sorted(String sortingType, String dataType, String outputFile);
}

class Input implements Sorting {
    private final ArrayList<String> input = new ArrayList<>();

    @Override
    public void input(String inputFile) throws IOException {

            while (scanner.hasNext()) {
                String s = scanner.next();
                input.add(s);
            }
            File file = new File("out.txt");
            file.createNewFile();
        scanner.close();
    }

    @Override
    public void sorted(String sortingType, String dataType, String outputFile) {
        switch (sortingType) {
            case "natural":
                SortingNatural sortingNatural = new SortingNatural();
                sortingNatural.sorted(input, dataType);
                break;
            case "byCount":
                SortingByCount sortingByCount = new SortingByCount();
                sortingByCount.sorted(input, dataType);
                break;
        }

    }
}

class InputLine implements Sorting {
    private final ArrayList<String> input = new ArrayList<>();

    @Override
    public void input(String inputFile) {
        if (!inputFile.isEmpty()) {
            try {
                File file = new File(inputFile);
                FileReader fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);
                String line = reader.readLine();
                input.add(line);
                while (line != null) {
                    line = reader.readLine();
                    input.add(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                input.add(line);
            }

        }
        scanner.close();
    }

    @Override
    public void sorted(String sortingType, String dataType, String outputFile) {
        switch (sortingType) {
            case "natural":
                SortingNatural sortingNatural = new SortingNatural();
                sortingNatural.sorted(input, dataType);
                break;
            case "byCount":
                SortingByCount sortingByCount = new SortingByCount();
                sortingByCount.sorted(input, dataType);
                break;
        }
    }
}



