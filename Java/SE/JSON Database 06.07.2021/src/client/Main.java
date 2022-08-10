package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    @Parameter(names = {"-t"})
    String type;

    @Parameter(names = {"-k"})
    String index = "";

    @Parameter(names = {"-v"})
    String value = "";

    @Parameter(names = {"-in"})
    String fileName = "";

    public static void main(String[] args) {
        Main main = new Main();
        JCommander.newBuilder().addObject(main).build().parse(args);
        Request request = new Request();
        System.out.println("Client started!");
        request.postGet(main.fileName.length() > 0 ? main.readFile(main.fileName) : main.getJson());
    }

    private String readFile(String fileName) {
        try (FileInputStream fileInputStream = new FileInputStream(
                String.format("F:\\Java\\JSON Database\\JSON Database\\task\\src\\client\\data\\%s", fileName))) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = fileInputStream.read(); i != -1; i = fileInputStream.read()) {
                stringBuilder.append((char) i);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getJson() {
      Map<String, String> args = new LinkedHashMap<>();
      args.put("type", type);
      if (index.length() != 0)
          args.put("key", index);
      if (value.length() != 0)
          args.put("value", value);

      return new Gson().toJson(args);
    }

}
