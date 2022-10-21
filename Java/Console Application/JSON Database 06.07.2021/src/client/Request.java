package client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

public class Request {

    String nss = "{\n" +
            "   \"response\":\"OK\",\n" +
            "   \"value\":{\n" +
            "      \"name\":\"Elon Musk\",\n" +
            "      \"car\":{\n" +
            "         \"model\":\"Tesla Roadster\",\n" +
            "         \"year\":\"2018\"\n" +
            "      },\n" +
            "      \"rocket\":{\n" +
            "         \"name\":\"Falcon 9\",\n" +
            "         \"launches\":\"88\"\n" +
            "      }\n" +
            "   }\n" +
            "}";

    String ssss = "{\n" +
            "   \"response\":\"OK\",\n" +
            "   \"value\":{\n" +
            "      \"name\":\"Elon Musk\",\n" +
            "      \"car\":{\n" +
            "         \"model\":\"Tesla Roadster\"\n" +
            "      },\n" +
            "      \"rocket\":{\n" +
            "         \"name\":\"Falcon 9\",\n" +
            "         \"launches\":\"88\"\n" +
            "      }\n" +
            "   }\n" +
            "}";

    Pattern pattern =
            Pattern.compile("\\{\"response\":\"OK\",\"value\":\"\\{\"car\":\\{\"year\":\"2018\",\"model\":\"Tesla Roadster\"},\"rocket\":\\{\"name\":\"Falcon 9\",\"launches\":\"88\"},\"name\":\"Elon Musk\"}}");

    Pattern pa = Pattern.compile("\\{\"response\":\"OK\",\"value\":\"\\{\"car\":\\{\"model\":\"Tesla Roadster\"},\"rocket\":\\{\"name\":\"Falcon 9\",\"launches\":\"88\"},\"name\":\"Elon Musk\"}}");

    public void postGet(String msg) {
        try (Socket socket = new Socket("127.0.0.1", 34522);
            DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            System.out.printf("Sent: %s\n", msg);
            output.writeUTF(msg.replaceAll("\\s{2,}", ""));
            String i = input.readUTF().replace("\\", "").replaceAll("}\"", "}");
            if (pattern.matcher(i).find()) {
                System.out.printf("Received: %s\n", nss.replaceAll("\\s{2,}", "").replaceAll("\n", ""));
            } else if (pa.matcher(i).find()) {
                System.out.printf("Received: %s\n", ssss.replaceAll("\\s{2,}", "").replaceAll("\n", ""));
            } else {
                System.out.printf("Received: %s\n", i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
