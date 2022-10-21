package contacts;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Save {

    private static final long serialVersionUID = 21L;
    private PhoneBook phoneBook;

    Save(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }


    private void writeObject(ObjectOutputStream objectOutputStream) {
        try {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeObject(phoneBook);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) {
        try {
            objectInputStream.readObject();
            phoneBook = (PhoneBook) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
    }


}
