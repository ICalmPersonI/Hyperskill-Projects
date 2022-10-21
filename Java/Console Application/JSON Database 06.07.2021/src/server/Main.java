package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) {
       new Main().runServer();
    }


    /*
    private void runServer() {
        System.out.println("Server started!");
        DataBase dataBase = Utils.load();
        try (ServerSocket serverSocket = new ServerSocket(34522)) {
            while (true) {
                Session session = new Session(serverSocket.accept(), dataBase);
                session.run();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */



    private void runServer() {
        System.out.println("Server started!");
        DataBase dataBase = Utils.load();
        try (ServerSocket serverSocket = new ServerSocket(34522)) {
            while (true) {
               Session session = new Session(serverSocket.accept(), dataBase);
               session.start();
               session.join();
               if (session.isServerShutdown())
                   break;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }



}

