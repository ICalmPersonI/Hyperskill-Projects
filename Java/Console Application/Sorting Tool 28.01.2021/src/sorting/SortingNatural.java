package sorting;

import java.util.ArrayList;

public class SortingNatural {
    void sorted(ArrayList<String> arrayList, String dataType) {
        switch (dataType) {
            case "long":
                long[] temp = insertionSort(numbersConvert(arrayList));
                System.out.print("Total numbers: " + arrayList.size()
                        + "\nSorted data: ");
                for (long l : temp) {
                    System.out.print(l + " ");
                }
                break;
            case "number":
                break;
            case "word":
                break;
            case "line":
                break;
        }
    }

    private static long[] numbersConvert(ArrayList<String> arrayList) {
        long[] output = new long[arrayList.size()];
        for (int s = 0; s < output.length; s++) {
            output[s] = Integer.parseInt(arrayList.get(s));
        }
        return output;
    }

    private static long[] insertionSort(long[] array) {
        for (int i = 1; i < array.length; i++) {
            long elem = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > elem) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = elem;
        }
        return array;
    }
}
