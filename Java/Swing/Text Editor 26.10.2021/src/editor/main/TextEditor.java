package editor.main;

import editor.panes.ActionPane;
import editor.panes.TextBoardPane;

import javax.swing.*;
import java.awt.*;

public class TextEditor extends JFrame {

    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        setVisible(true);
        setTitle("Text Editor");
        setResizable(false);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());


        TextBoardPane textBoardPane = new TextBoardPane();
        ActionPane actionPane = new ActionPane(textBoardPane.getTextArea(), this);


        contentPane.add(actionPane, BorderLayout.NORTH);
        contentPane.add(textBoardPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(actionPane.getFileMenu());
        menuBar.add(actionPane.getSearchMenu());
        setJMenuBar(menuBar);

        add(contentPane, BorderLayout.CENTER);
    }



}
