package server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FilesList {
    private Map<Integer, String> storage = new HashMap<>();

    void add(int id, String name) {
        storage.put(id, name);
    }

    boolean containsById(int id) {
        return storage.containsKey(id);
    }

    boolean containsByName(String name) {
        return storage.containsValue(name);
    }

    String getFileNameById(int id) {
        return storage.get(id);
    }

    void removeById(int id) {
        storage.remove(id);
    }

    void removeByName(String name) {
        int id = storage.entrySet().stream()
                .filter(elem -> elem.getValue().equals(name))
                .findFirst().get().getKey();
        storage.remove(id, name);
    }

    int getId() {
       return storage.keySet().stream().mapToInt(value -> value).max().orElse(0) + 1;
    }

    void serializing() {
        try {
            FileOutputStream myFileOutStream = new FileOutputStream(
                    "F:\\Java\\File Server\\File Server\\task\\src\\server\\filesList.map");

            ObjectOutputStream myObjectOutStream
                    = new ObjectOutputStream(myFileOutStream);

            myObjectOutStream.writeObject(storage);

            myObjectOutStream.close();
            myFileOutStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void deserialization() {
        try {
            FileInputStream fileInput = new FileInputStream(
                    "F:\\Java\\File Server\\File Server\\task\\src\\server\\filesList.map");

            ObjectInputStream objectInput
                    = new ObjectInputStream(fileInput);

            storage = (HashMap)objectInput.readObject();

            objectInput.close();
            fileInput.close();
        } catch (IOException obj1) {
            obj1.printStackTrace();
        } catch (ClassNotFoundException obj2) {
            System.out.println("Class not found");
            obj2.printStackTrace();
        }

    }
}
