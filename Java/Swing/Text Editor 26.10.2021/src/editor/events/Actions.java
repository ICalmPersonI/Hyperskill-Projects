package editor.events;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Actions {

    private final JTextArea textArea;
    private final JTextArea pattern;
    private final JFileChooser jfc;

    private List<WordRange> ranges;
    private int index = 0;


    public Actions(JTextArea pattern, JTextArea textArea, JFileChooser jfc) {
        this.pattern = pattern;
        this.textArea = textArea;
        this.jfc = jfc;
    }


    public ActionListener getAction(Events event) {
        switch (event) {
            case SAVE:
                return e -> {
                    jfc.setDialogTitle("Save");

                    int returnValue = jfc.showSaveDialog(null);

                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = jfc.getSelectedFile();
                        save(textArea.getText(), selectedFile);
                    }

                };
            case LOAD:
                return e -> {
                    jfc.setDialogTitle("Load");

                    int returnValue = jfc.showOpenDialog(null);

                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = jfc.getSelectedFile();
                        String text = load(selectedFile);
                        textArea.setText(text);
                    }
                };

            case SEARCH:
                return e -> {
                    ranges = getWordRanges();
                    index = 0;
                    select();
                };

            case ARROW_RIGHT:
                return e -> {
                    if (index + 1 < ranges.size()) {
                        index++;
                    }
                    select();
                };

            case ARROW_LEFT:
                return e -> {
                    index = index - 1 > -1 ? index-- : ranges.size() - 1;
                    select();
                };

            default:
                return null;
        }
    }

    private void select() {
        WordRange range = ranges.get(index);
        textArea.setSelectionStart(range.start);
        textArea.setSelectionEnd(range.end);
        textArea.grabFocus();
    }


    private List<WordRange> getWordRanges() {
        List<WordRange> ranges = new ArrayList<>();
        String text = textArea.getText();
        String patternText = pattern.getText();

        List<String> groups = new ArrayList<>();
        Matcher matcher = Pattern.compile(patternText)
                .matcher(text);
        while (matcher.find()) {
            groups.add(matcher.group());
        }

        for (String word : groups) {
            for (int i = 0; i < text.length(); i++) {

                if (word.charAt(0) == text.charAt(i)) {

                    for (int j = i, s = 0; ; j++, s++) {

                        if (word.charAt(s) != text.charAt(j)) {
                            break;
                        }

                        if (s == word.length() - 1) {
                            ranges.add(new WordRange(i, j + 1));
                            break;
                        }
                    }
                }
            }
        }

        return ranges;
    }


    private String load(File file) {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            for (int c = fileInputStream.read(); c != -1; c = fileInputStream.read()) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private void save(String text, File file) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

class WordRange {
    final int start;
    final int end;

    WordRange(int start, int end) {
        this.start = start;
        this.end = end;
    }
}



