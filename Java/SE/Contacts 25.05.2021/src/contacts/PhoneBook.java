package contacts;

import java.io.*;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PhoneBook implements Serializable {

    private Map<Integer, Record> records = new HashMap<>();

    transient private String fileName;

    PhoneBook(String fileName) {
        this.fileName = fileName;
        if (fileName.length() > 0) {
            if (new File(fileName).exists()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(fileName);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    records = (Map<Integer, Record>) objectInputStream.readObject();
                } catch (IOException | ClassNotFoundException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    public void save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(records);
            fileOutputStream.close();
            objectOutputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    public void add(Record record) {
        int id = records.keySet().stream().max(Integer::compareTo).orElse(0);
        records.put(++id, record);
    }

    public void replace(Record record, int id) {
        records.put(id, record);
    }

    public boolean remove(String name) {
        Map.Entry<Integer, Record> id = records
                .entrySet()
                .stream()
                .filter(f -> f.getValue().getName().matches(name))
                .findFirst()
                .orElse(null);
        if (id != null) {
            records.remove(id.getKey());
            Map<Integer, Record> recordMap = new HashMap<>();
            for (Map.Entry<Integer, Record> record : records.entrySet()) {
                recordMap.put(record.getKey() - 1, record.getValue());
            }
            records = recordMap;
            return true;
        }
       return false;
    }

    public Record get(int id) {
        return records.getOrDefault(id, null);
    }

    public int count() {
        return records.size();
    }

    public void print() {
        records.forEach((id, record) -> System.out.printf("%s. %s\n",
                id, record.getName() +
                        (record.getClass() == Person.class ? String.format(" %s", record.getSurname()) : "")));
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

    public Map<Integer, Record> search(String line) {
        List<Function<Record, String>> functionList = List.of(
                Record::getName,
                Record::getNumber
        );

        Map<Integer, Record> foundsRecord = new HashMap<>();
        for (Function<Record, String> function : functionList ) {
            foundsRecord = records.entrySet()
                    .stream()
                    .filter(f -> Pattern.compile(line.toLowerCase()).matcher(function.apply(f.getValue()).toLowerCase()).find())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if (!foundsRecord.isEmpty()) break;
        }
        return foundsRecord;
    }
}
