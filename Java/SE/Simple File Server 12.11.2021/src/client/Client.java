package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final String address = "127.0.0.1";
    private final int port = 23455;
    private final String clientDataPath = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "client" + File.separator + "data" + File.separator;

    private final Socket socket = new Socket(InetAddress.getByName(address), port);
    private final DataInputStream input = new DataInputStream(socket.getInputStream());
    private final DataOutputStream output = new DataOutputStream(socket.getOutputStream());


    Client() throws IOException { }

    String getFile(String nameOrId) throws IOException {
        output.writeUTF("GET");

        output.writeUTF(nameOrId);
        int statusCode = input.read();

        if (statusCode == 200) {
            int length = input.read();
            byte[] content = input.readNBytes(length);

            System.out.println("The file was downloaded! Specify a name for it:");
            String nameToBeSaved = new Scanner(System.in).nextLine();
            saveFile(nameToBeSaved, content);

            return "File saved on the hard drive!\n";

        } else return "The response says that this file is not found!";

    }

    String sentFile(String fileName, String fileNameToBeSaved) throws IOException {
        output.writeUTF("PUT");

        output.writeUTF(fileNameToBeSaved);
        byte[] content = readFile(new File(clientDataPath + fileName)).getBytes();
        output.write(content.length);
        output.write(content);
        int statusCode = input.read();

        if (statusCode == 200) {
            int fileId = input.read();
            return String.format("Response says that file is saved! ID = %d", fileId);
        } else return "The response says that creating the file was forbidden!";

    }

    String deleteFile(String nameOrId) throws IOException {
        output.writeUTF("DELETE");

        output.writeUTF(nameOrId);
        int statusCode = input.read();

        return statusCode == 200 ? "The response says that the file was successfully deleted!"
                : "The response says that the file was not found!";
    }

    private void saveFile(String name, byte[] content) {
        File file = new File(clientDataPath + name);

        try {
            System.out.println(file.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            for (int i = fileInputStream.read(); i != -1; i = fileInputStream.read()) {
                sb.append((char) i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    void shutdownServer() throws IOException {
        output.writeUTF("SHUTDOWN");
    }

    void closeSocket() throws IOException {
        socket.close();
    }

}
