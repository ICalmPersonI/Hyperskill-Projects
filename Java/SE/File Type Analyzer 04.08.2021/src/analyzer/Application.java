package analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class Application {

    private Map<String, String> readFiles(File path) {
        Map<String, String> files = new HashMap<>();
        for (File file : Objects.requireNonNull(path.listFiles())) {
            files.put(file.getName(), readFile(file));
        }
        return files;
    }

    private String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream input = new FileInputStream(file)) {
            for (int i = input.read(); i != -1; i = input.read()) {
                sb.append((char) i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public void run(File path, File patterns) {
        List<Pattern> patternList = new ArrayList<>();
        Stream.of(readFile(patterns)
                .split("\n"))
                .map(elem -> elem.replace("\"", ""))
                .forEach(elem -> {
            String[] temp = elem.split(";");
            patternList.add(new Pattern(Integer.parseInt(temp[0]), temp[1], temp[2]));
        });
        patternList.sort(new SortByPriority());

        Map<String, String> files = readFiles(path);
        SearchByPattern search = new SearchByPattern();
        Collection<Callable<String>> tasks = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(files.size());

        files.forEach((key, value) -> tasks.add(() -> {
            String verdict = "Unknown file type";
                    for (Pattern pattern : patternList) {
                        if (search.start(value, pattern.getValue())) {
                            verdict = pattern.getName();
                        }
                    }
                    return key + ": " + verdict;
                }
        ));

        try {
            executorService.invokeAll(tasks);

            StringBuilder result = new StringBuilder();
            tasks.forEach(elem -> {
                try {
                    result.append(elem.call()).append("\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
