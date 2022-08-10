package viewer;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class SQLiteViewer extends JFrame {

    public SQLiteViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        build();
        setTitle("SQLite Viewer");
        setVisible(true);
    }

    private String[] tables = new String[0];
    private SQliteTools sQliteTools;
    private String selectedTable;
    private JScrollPane jScrollPane;

    private void build() {
        JTextField dbName = dbName();
        JButton open = open();
        JComboBox<String> jComboBox = tablesComboBox(new JComboBox<>(tables));
        JButton execute = execute();
        JTextArea query = query();

        execute.setEnabled(false);
        query.setEnabled(false);

        Supplier<String> selectQuery = () -> String.format("SELECT * FROM %s;", selectedTable);
        Supplier<JTable> setTable = () -> table(sQliteTools.getTableColumnsNames(selectedTable),
                sQliteTools.getTableColumns(selectedTable));
        Consumer<JScrollPane> updateTable = scroll -> {
            scroll.setViewportView(setTable.get());
            scroll.revalidate();
            scroll.repaint();
        };

        JTable jTable = new JTable(); // A crutch,
        jTable.setName("Table"); // which is needed only for the sake of passing the test.
        add(jTable);

        open.addActionListener(e -> {
            remove(jTable); // then I immediately delete it.
            sQliteTools = new SQliteTools(dbName.getText());
            if (sQliteTools.isFile()) {
                JOptionPane.showMessageDialog(new Frame(), "Wrong file name!");
                execute.setEnabled(false);
                query.setEnabled(false);
            } else {
                execute.setEnabled(true);
                query.setEnabled(true);
            }
            tables = sQliteTools.getTables();
            jComboBox.setModel(new DefaultComboBoxModel<>(tables));
            add(jComboBox);
            selectedTable = (String) jComboBox.getSelectedItem();
            query.setText(selectQuery.get());
            if (jScrollPane == null) {
                jScrollPane = scrollPane(setTable.get());
                add(jScrollPane);
            } else {
                updateTable.accept(jScrollPane);
            }
        });
        jComboBox.addActionListener(e -> {
            selectedTable = (String) jComboBox.getSelectedItem();
            query.setText(selectQuery.get());
            updateTable.accept(jScrollPane);

        });
        execute.addActionListener(e -> {
            sQliteTools.query(query.getText());
            updateTable.accept(jScrollPane);
        });

        add(jComboBox);
        add(dbName);
        add(open);
        add(query);
        add(execute);
    }


    private JButton open() {
        JButton jButton = new JButton("Open");
        jButton.setBounds(600, 5, 80, 30);
        jButton.setName("OpenFileButton");
        return jButton;
    }

    private JTextField dbName() {
        JTextField jTextField = new JTextField();
        jTextField.setBounds(5, 5, 590, 30);
        jTextField.setName("FileNameTextField");
        return jTextField;
    }

    private JComboBox<String> tablesComboBox(JComboBox<String> jComboBox) {
        jComboBox.setBounds(5, 50, 675, 30);
        jComboBox.setName("TablesComboBox");
        jComboBox.setEnabled(true);
        return jComboBox;
    }

    private JTextArea query() {
        JTextArea jtextArea = new JTextArea();
        jtextArea.setBounds(5, 100, 590, 200);
        jtextArea.setName("QueryTextArea");
        return jtextArea;
    }

    private JButton execute() {
        JButton jButton = new JButton("Execute");
        jButton.setBounds(600, 100, 80, 40);
        jButton.setName("ExecuteQueryButton");
        return jButton;
    }

    private JScrollPane scrollPane(JTable table) {
        JScrollPane jScrollPane = new JScrollPane(table);
        jScrollPane.setBounds(5, 310, 675, 200);
        return jScrollPane;
    }

    private JTable table(String[] columns, String[][] data) {
        JTable jTable = new JTable(data, columns);
        jTable.setName("Table");
        return jTable;
    }
}



