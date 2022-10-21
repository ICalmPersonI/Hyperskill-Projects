package carsharing;

import java.util.List;
import java.util.Scanner;

public class DaoImplements implements Dao {

    private final Db dataBase;

    DaoImplements(String dbName) {
        dataBase = new Db(dbName);
    }

    @Override
    public void addCarToRecord(String sql, String car) {
        if (dataBase.update(sql)) {
            System.out.printf("\nYou rented '%s'\n", car);
        }
    }

    @Override
    public boolean updateRecord(String sql) {
        return dataBase.update(sql);
    }

    @Override
    public void createRecord(String sql, String table) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("\nEnter the %s name:\n", table);
        if (dataBase.insert(String.format(sql, scanner.nextLine()))) {
            System.out.printf("The %s was %s!\n\n", table, table.matches("company") ? "created" : "added");
        }
    }

    @Override
    public List<Record> getAllRecords(String sql, String ...fkColumnName) {
        return fkColumnName.length > 0 ? dataBase.select(sql, fkColumnName) : dataBase.select(sql);
    }

}
