package viewer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQliteTools {

    private Connection connection;
    private Statement statement;
    private final String bdName;

    SQliteTools(String bdName) {
        this.bdName = bdName;
    }

    public boolean isFile() {
        return !(new File(bdName).exists());
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:file:%s", bdName));
            statement = connection.createStatement();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private void close() {
        try {
            statement.close();
            connection.close();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public void query(String query) {
        connect();
        try {
            statement.executeQuery(query);
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(new Frame(), "ERROR MESSAGE");
            se.printStackTrace();
        }
        close();
    }

    public String[][] getTableColumns(String table) {
        List<List<String>> data = new ArrayList<>();
        connect();
        int columnCount = 0;
        try {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM %s", table));
            ResultSetMetaData rsmd = resultSet.getMetaData();
            columnCount = rsmd.getColumnCount();
            while (resultSet.next()) {

                List<String> lines = new ArrayList<>();
                for (int i = 1; i < columnCount + 1; i++) {
                    lines.add(resultSet.getString(i));
                }
                data.add(lines);

            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        close();

        return toArray(data, columnCount);
    }

    private String[][] toArray(List<List<String>> list, int columnCount) {
        String[][] array = new String[list.size()][columnCount];
        for (int i = 0; i < array.length; i++) {
            for (int s = 0; s < array[i].length; s++) {
                array[i][s] = list.get(i).get(s);
            }
        }
        return array;
    }

    public String[] getTableColumnsNames(String table) {
        List<String> columnsNames = new ArrayList<>();
        connect();
        try {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM %s", table));
            ResultSetMetaData rsmd = resultSet.getMetaData();
            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                columnsNames.add(rsmd.getColumnName(i));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        close();
        return columnsNames.toArray(new String[0]);
    }

    public String[] getTables() {
        List<String> tablesList = new ArrayList<>();
        connect();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master " +
                    "WHERE type ='table' AND name NOT LIKE 'sqlite_%';");
            while(resultSet.next()) {
                tablesList.add(resultSet.getString(1));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        close();
        return tablesList.toArray(new String[0]);
    }
}
