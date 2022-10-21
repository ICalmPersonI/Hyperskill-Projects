package sorting;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(final String[] args) throws IOException {
        selectConfiguration(args);
    }

    private static void selectConfiguration(String[] args) throws IOException {
        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));
        argsList.add(" ");
        argsList.add(" ");
        boolean sortingTypeCheck = false;
        boolean dataTypeCheck = false;
        for (int i = 0; i < argsList.size(); i++) {
            if (argsList.get(i).matches("-sortingType")) { ;
                if (argsList.get(i + 1).matches("\\w++")) {
                    sortingTypeCheck = true;
                }
            } else {
                sortingTypeCheck = true;
            }
            if (argsList.get(i).matches("-dataType")) { ;
                if (argsList.get(i + 1).matches("\\w++")) {
                    dataTypeCheck = true;
                }
            } else {
                dataTypeCheck = true;
            }
        }
        if (!sortingTypeCheck) {
            System.out.println("No data type defined!");
            System.exit(0);
        }
        if (!dataTypeCheck) {
            System.out.println("No sorting type defined!");
            System.exit(0);
        }
        String sortingType = "natural";
        String dataType = "";
        String pathInput = "";
        String pathOutput = "";
        for (int i = 0; i < argsList.size(); i++) {
            if (argsList.get(i).matches("-inputFile")) {
                pathInput = argsList.get(i + 1);
            }
            if (argsList.get(i).matches("-outputFile")) {
                pathOutput = argsList.get(i + 1);

            }
        }
        for (String s : args) {
            if (s.matches("natural")) {
                sortingType = "natural";
            }
            if (s.matches("byCount")) {
                sortingType = "byCount";
            }

        }
        for (String s : args) {
            if (s.matches("long")) {
                dataType = "long";
            } else if (s.matches("word")) {
                dataType = "word";
            } else if (s.matches("line")){
               dataType = "line";
            }
        }


        if (dataType.matches("line")) {
            Sorting sortingLine = new InputLine();
            sortingLine.input(pathInput);
            sortingLine.sorted(sortingType, dataType, pathOutput);
        } else {
            Sorting sorting = new Input();
            sorting.input(pathInput);
            sorting.sorted(sortingType, dataType, pathOutput);
        }

    }

}
