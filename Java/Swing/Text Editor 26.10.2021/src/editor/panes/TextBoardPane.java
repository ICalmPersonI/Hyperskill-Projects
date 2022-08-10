package editor.panes;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TextBoardPane extends JPanel {

    private final JTextArea textArea = new JTextArea();

    public TextBoardPane() {
        textArea.setName("TextArea");
        textArea.setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        textArea.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setName("ScrollPane");



        add(scrollPane);
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
