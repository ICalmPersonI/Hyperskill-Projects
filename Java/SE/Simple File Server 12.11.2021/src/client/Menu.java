package client;

import java.io.IOException;
import java.util.Scanner;

public class Menu {

    private final Client client = new Client();
    private final Scanner scanner = new Scanner(System.in);

    Menu() throws IOException { }

    void start() throws IOException {
        System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file):");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1": {
                getFile();
                System.out.println();
                break;
            }
            case "2": {
                createFile();
                System.out.println();
                break;
            }
            case "3": {
                deleteFile();
                System.out.println();
                break;
            }
            case "exit": {
                client.shutdownServer();
            }
        }
        client.closeSocket();
    }

    private void getFile() throws IOException {
        String nameOrId = formFileBy();
        System.out.println(client.getFile(nameOrId));
    }

    private void deleteFile() throws IOException {
        String nameOrId = formFileBy();
        System.out.println(client.deleteFile(nameOrId));
    }

    private void createFile() throws IOException {
        System.out.print("Enter name of the file:");
        String fileName = scanner.nextLine();
        System.out.print("Enter name of the file to be saved on server:");
        String nameToBeSaved = scanner.nextLine();
        System.out.println("The request was sent.");

        System.out.println(client.sentFile(fileName, nameToBeSaved));
    }

    private String formFileBy() {
        System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id):");
        String nameOrId = "";
        switch (scanner.nextLine()) {
            case "1": {
                System.out.print("Enter filename:");
                nameOrId  = scanner.nextLine();
                break;
            }
            case "2": {
                System.out.print("Enter id:");
                nameOrId  = scanner.nextLine();

                break;
            }
        }
        System.out.println("The request was sent.");
        return nameOrId ;
    }
}
