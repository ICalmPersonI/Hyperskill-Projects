package sorting;

import java.util.*;
import java.util.stream.Collectors;

public class SortingByCount {
    void sorted(ArrayList<String> arrayList, String dataType) {
        switch (dataType) {
            case "long":
               sortedLong(arrayList);
                break;
            case "number":
                break;
            case "word":
                sortedWord(arrayList);
                break;
            case "line":
                sortedLine(arrayList);
                break;
        }
    }
    private void sortedLine(ArrayList<String> arrayList) {
        Map<String, Integer> lineMap = fillingMapString(arrayList);
        ArrayList<Integer> percent = new ArrayList<>();
        for (int values : lineMap.values()) {
            double size = arrayList.size();
            double temp = Math.round(((double) values / size) * 100);
            percent.add((int) temp);
        }
        Collections.sort(percent);
        List<String> key = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        lineMap.forEach((n,m) -> values.add(m));
        lineMap.forEach((n,m) -> key.add(n));

        System.out.println("Total lines: " + arrayList.size());
        for (int i = 0; i < percent.size(); i++) {
            System.out.println( key.get(i) + ": "
                    + values.get(i) + " time(s), "
                   + percent.get(i) + "%");
        }
    }
    private void sortedWord(ArrayList<String> arrayList) {
      Map<String, Integer> wordMap = fillingMapString(arrayList);
        ArrayList<Integer> percent = new ArrayList<>();
        for (int values : wordMap.values()) {
            double size = arrayList.size();
            double temp = Math.round(((double) values / size) * 100);
            percent.add((int) temp);
        }
        Collections.sort(percent);
        List<String> sortedKey = wordMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        List<Integer> values = new ArrayList<>(wordMap.values());
        System.out.println("Total numbers: " + arrayList.size());
        for (int i = 0; i < values.size(); i++) {
            System.out.println( sortedKey.get(i) + ": "
                    + values.get(i) + " time(s), "
                    + percent.get(i) + "%");
        }
    }

    private void sortedLong(ArrayList<String> arrayList){
        List<Long> longList = new ArrayList<>();
        arrayList.forEach(a -> longList.add(Long.parseLong(a)));
        Map<Long, Integer> longMap = fillingMapLong(longList);
        ArrayList<Integer> percent = new ArrayList<>();
        for (int values : longMap.values()) {
            double size = arrayList.size();
            double temp = Math.round(((double) values / size) * 100);
            percent.add((int) temp);
        }
        Collections.sort(percent);
        List<Integer> sortValues = new ArrayList<>(longMap.values());
        Collections.sort(sortValues);
        List<Long> sortedKey = new ArrayList<>();
        List<Long> finalSortedKey = sortedKey;
        longMap.forEach((n, m) -> finalSortedKey.add(n));
        sortedKey = longMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        System.out.println("Total numbers: " + arrayList.size());
        for (int i = 0; i < sortValues.size(); i++) {
            System.out.println( sortedKey.get(i) + ": "
                    + sortValues.get(i) + " time(s), "
                    + percent.get(i) + "%");
        }
    }

    private static Map<String, Integer> fillingMapString(List<String> arrayList) {
        Map<String, Integer> temp = new TreeMap<>();
        int count = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            for (int s = 0; s < arrayList.size(); s++) {
                if (arrayList.get(i).equals(arrayList.get(s))) {
                    count++;
                }
            }
            temp.put(arrayList.get(i), count);
            count = 0;
        }
        return temp;
    }

    private static Map<Long, Integer> fillingMapLong(List<Long> arrayList) {
        Map<Long, Integer> temp = new TreeMap<>();
        int count = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            for (int s = 0; s < arrayList.size(); s++) {
                if (arrayList.get(i).equals(arrayList.get(s))) {
                    count++;
                }
            }
            temp.put(arrayList.get(i), count);
            count = 0;
        }
        return temp;
    }

}
