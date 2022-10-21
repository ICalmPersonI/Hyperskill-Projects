package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Db {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private final String url;

    private Connection conn;
    private Statement statement;

    Db(String dbName) {
        this.url = String.format("jdbc:h2:file:" +
                "F:\\Java\\Car Sharing\\Car Sharing\\task\\src\\carsharing\\db\\%s", dbName);

    }

    private void connect() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(true);
            statement = conn.createStatement();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    private void close() {
        try {
            statement.close();
            conn.close();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public void createTable() {
        connect();
        try {
            String sql = "CREATE TABLE company" +
                    "(id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) UNIQUE NOT NULL" +
                    ");" +
                    "CREATE TABLE car" +
                    "(id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) UNIQUE NOT NULL," +
                    "company_id INTEGER NOT NULL," +
                    "CONSTRAINT fk_company_id FOREIGN KEY(company_id)" +
                    "REFERENCES company(id)" +
                    ");" +
                    "CREATE TABLE customer" +
                    "(id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) UNIQUE NOT NULL," +
                    "rented_car_id INTEGER DEFAULT NULL," +
                    "CONSTRAINT fk_rented_car_id FOREIGN KEY(rented_car_id)" +
                    "REFERENCES car(id)" +
                    ");";
            statement.executeUpdate(sql);
        } catch (Exception se) {
            se.printStackTrace();
        }
        close();
    }

    public boolean insert(String sql) {
        connect();
        try {
            statement.executeUpdate(sql);
        } catch (Exception se) {
            se.printStackTrace();
            close();
            return false;
        }
        close();
        return true;
    }

    public List<Record> select(String sql, String... fk_id) {
        List<Record> list = new ArrayList<>();
        connect();
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(new Record(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        fk_id.length > 0 ? resultSet.getInt(fk_id[0]) : 0));
            }

        } catch (Exception se) {
            se.printStackTrace();
        }
        close();
        return list;
    }

    public boolean update(String sql) {
        connect();
        try {
            statement.executeUpdate(sql);
        } catch (Exception se) {
            se.printStackTrace();
            close();
            return false;
        }
        close();
        return true;
    }

}
