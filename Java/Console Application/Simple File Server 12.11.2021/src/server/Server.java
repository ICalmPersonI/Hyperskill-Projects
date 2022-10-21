package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Server {

    private final String address = "127.0.0.1";
    private final int port = 23455;
    private final String filesPath = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;

    private ServerSocket server;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    private final FilesList filesList = new FilesList();


    Server() throws IOException {
        filesList.deserialization();
        reloadConnect();
    }

    void start() throws IOException {

        while (true) {
            String command = input.readUTF();
            switch (command) {
                case "GET": {
                    Consumer<String> sentFile = name -> {
                        File file = new File(filesPath + name);
                        try {
                            output.write(200);
                            byte[] content = readFile(file).getBytes();
                            output.write(content.length);
                            output.write(content);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    };

                    String fileNameOrId = input.readUTF();

                    if (isId(fileNameOrId)) {
                        int id = Integer.parseInt(fileNameOrId);
                        if (!filesList.containsById(id)) {
                            output.write(404);
                        } else {
                            String fileName = filesList.getFileNameById(id);
                            sentFile.accept(fileName);
                        }
                    } else {
                        String fileName = fileNameOrId;
                        if (!filesList.containsByName(fileName)) {
                            output.write(404);
                        } else {
                            sentFile.accept(fileName);
                        }
                    }
                    break;
                }
                case "PUT": {
                    String receivedFileName = input.readUTF();
                    String fileName = receivedFileName.length() == 0 ? generateName() : receivedFileName;

                    int length = input.read();
                    byte[] content = input.readNBytes(length);

                    if (!filesList.containsByName(fileName)) {
                        File file = new File(filesPath + fileName);

                        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                            fileOutputStream.write(content);
                            output.write(200);

                            int id = filesList.getId();
                            output.write(id);
                            filesList.add(id, fileName);
                        } catch (IOException e) {
                            e.printStackTrace();
                            output.write(404);
                        }
                    } else {
                        output.write(403);
                    }
                    break;
                }

                case "DELETE": {
                    Consumer<String> deleteFile = name -> {
                        File file = new File(filesPath + name);
                        file.delete();
                    };

                    String fileNameOrId = input.readUTF();

                    if (isId(fileNameOrId)) {
                        int id = Integer.parseInt(fileNameOrId);
                        if (filesList.containsById(id)) {
                            String fileName = filesList.getFileNameById(id);
                            filesList.removeById(id);
                            deleteFile.accept(fileName);
                            output.write(200);
                        } else {
                            output.write(404);
                        }
                    } else {
                        String fileName = fileNameOrId;
                        if (filesList.containsByName(fileName)) {
                            filesList.removeByName(fileName);
                            deleteFile.accept(fileName);
                            output.write(200);
                        } else {
                            output.write(404);
                        }
                    }
                    break;
                }

                case "SHUTDOWN": {
                    serverShutdown();
                }
            }
            reloadConnect();
        }
    }

    private String generateName() {
        StringBuilder stringBuilder = new StringBuilder();
        int length = new Random().ints(5, 16).findFirst().getAsInt();
        Random letters = new Random();
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                stringBuilder.append((char) letters.ints(65, 91)
                        .findFirst().getAsInt());
            } else {
                stringBuilder.append((char) letters.ints(97, 123)
                        .findFirst().getAsInt());
            }
        }
        return stringBuilder.toString();
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

    private boolean isId(String nameOrId) {
        try {
            Integer.parseInt(nameOrId);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void reloadConnect() throws IOException {
        if (server != null) server.close();
        if (socket != null) socket.close();

        this.server = new ServerSocket(port, 50, InetAddress.getByName(address));
        this.socket = server.accept();
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
    }

    private void serverShutdown() throws IOException {
        socket.close();
        server.close();
        filesList.serializing();
        System.exit(0);
    }


}
