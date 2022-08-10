package editor.panes;

import editor.events.Actions;
import editor.events.Events;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionListener;

public class ActionPane extends JPanel {

    private final JMenu fileMenu;
    private final JMenu searchMenu;

    public ActionPane(JTextArea textArea, JFrame mainFrame) {
        setLayout(new BorderLayout());
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new FlowLayout());

        JTextArea pattern = patternArea();

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setName("FileChooser");

        add(jfc);

        Actions actions = new Actions(pattern, textArea, jfc);

        JCheckBox checkBox = new JCheckBox();
        checkBox.setName("UseRegExCheckbox");
        JLabel regex = new JLabel("Use regex");

        fileMenu = new FileMenu(mainFrame, actions);
        searchMenu = new SearchMenu(actions, checkBox);

        JButton save = createButton(
                resizeImage(".\\Text Editor\\task\\src\\icons\\save.png", 25, 25),
                "SaveButton",
                actions.getAction(Events.SAVE));
        JButton load = createButton(
                resizeImage(".\\Text Editor\\task\\src\\icons\\load.png", 25, 25),
                "OpenButton",
                actions.getAction(Events.LOAD));
        JButton arrowRight = createButton(
                resizeImage(".\\Text Editor\\task\\src\\icons\\right-arrow.png", 25, 25),
                "NextMatchButton",
                actions.getAction(Events.ARROW_RIGHT));
        JButton arrowLeft = createButton(
                resizeImage(".\\Text Editor\\task\\src\\icons\\left-arrow.png", 25, 25),
                "PreviousMatchButton",
                actions.getAction(Events.ARROW_LEFT));
        JButton search = createButton(
                resizeImage(".\\Text Editor\\task\\src\\icons\\search.png", 25, 25),
                "StartSearchButton",
                actions.getAction(Events.SEARCH));

        contentPane.add(save);
        contentPane.add(load);
        contentPane.add(pattern);
        contentPane.add(search);
        contentPane.add(arrowLeft);
        contentPane.add(arrowRight);
        contentPane.add(checkBox);
        contentPane.add(regex);

        add(contentPane);
    }

    private JButton createButton(ImageIcon icon, String name, ActionListener event) {
        JButton button = new JButton();
        button.setName(name);
        button.setIcon(icon);
        button.setPreferredSize(new Dimension(30, 40));
        button.addActionListener(event);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        return button;
    }

    private JTextArea patternArea() {
        final JTextArea textArea = new JTextArea(1, 30);
        textArea.setName("SearchField");
        textArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
        textArea.setBackground(Color.WHITE);
        final Border border = BorderFactory.createLineBorder(Color.BLACK);
        textArea.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        return textArea;
    }

    private ImageIcon resizeImage(String imagePath, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image image = imageIcon.getImage();
        return new ImageIcon(image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH));
    }

    public JMenu getFileMenu() {
        return fileMenu;
    }

    public JMenu getSearchMenu() {
        return searchMenu;
    }
}

class SearchMenu extends JMenu {

    SearchMenu(Actions actions, JCheckBox checkBox) {
        setText("Search");
        setName("MenuSearch");

        JMenuItem startSearch = new JMenuItem("Start search");
        JMenuItem previousSearch = new JMenuItem("Previous search");
        JMenuItem nextMatch = new JMenuItem("Next Match");
        JMenuItem useRegularExpression = new JMenuItem("Use regular expressions");
        startSearch.setName("MenuStartSearch");
        previousSearch.setName("MenuPreviousMatch");
        nextMatch.setName("MenuNextMatch");
        useRegularExpression.setName("MenuUseRegExp");

        startSearch.addActionListener(actions.getAction(Events.SEARCH));
        previousSearch.addActionListener(actions.getAction(Events.ARROW_LEFT));
        nextMatch.addActionListener(actions.getAction(Events.ARROW_RIGHT));
        useRegularExpression.addActionListener( a -> checkBox.setSelected(!checkBox.isSelected()));

        add(startSearch);
        add(previousSearch);
        add(nextMatch);
        add(useRegularExpression);
    }

}

class FileMenu extends JMenu {

    FileMenu(JFrame mainFrame, Actions actions) {
        setText("File");
        setName("MenuFile");

        JMenuItem save = new JMenuItem("Save");
        JMenuItem load = new JMenuItem("Load");
        JMenuItem exit = new JMenuItem("Exit");
        save.setName("MenuSave");
        load.setName("MenuOpen");
        exit.setName("MenuExit");

        save.addActionListener(actions.getAction(Events.SAVE));
        load.addActionListener(actions.getAction(Events.LOAD));
        exit.addActionListener(a -> mainFrame.dispose());

        add(save);
        add(load);
        add(exit);
    }
}
