package carsharing;


import java.util.List;

public interface Dao {
    void addCarToRecord(String sql, String car);
    boolean updateRecord(String sql);
    void createRecord(String sql, String table);
    List<Record> getAllRecords(String sql, String ...fkColumnName);
}
