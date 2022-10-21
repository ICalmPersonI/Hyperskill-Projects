package server;

import java.io.*;

public class Utils {

    public static void save(DataBase db) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(
                "F:\\Java\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json"))) {
            outputStream.writeObject(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DataBase load() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(
                "F:\\Java\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json"))) {
            return (DataBase) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new DataBase();
        }
    }
}
