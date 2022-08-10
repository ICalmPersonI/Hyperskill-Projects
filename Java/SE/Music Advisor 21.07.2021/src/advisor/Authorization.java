package advisor;

import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class Authorization {

    private final Server server = new Server();
    private final String CLIENT_ID = "23b071a57cb14509975ef2017e72aaa8";
    private final String ACCESS;

    Authorization(String access) {
        this.ACCESS = access;
    }

    public String getAccessToken() {
        try {
            System.out.println("use this link to request the access code:");
            System.out.println(get());
            System.out.println("waiting for code...");
            Thread.sleep(3000);
            System.out.println("code received\n" +
                    "Making http request for access_token...");
            System.out.println("Success!");
            server.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       return JsonParser.parseString(post().body()).getAsJsonObject().get("access_token").getAsString();
    }

    private String get() {
        String param = "client_id=" + CLIENT_ID + "&" +
                "response_type=code&" +
                "redirect_uri=http://localhost:8080&" +
                "show_dialog=true";
        HttpRequest get = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(ACCESS + "/authorize?" + param))
                .GET()
                .build();

        return get.toString();
    }

    private HttpResponse<String> post() {
        HttpClient client = HttpClient.newHttpClient();
        String base64 = Base64.getEncoder().encodeToString((CLIENT_ID + ":" + "3c7da7e3fa8d48f3b8fd6bab22119ace").getBytes());
        HttpRequest post = HttpRequest.newBuilder()
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + base64)
                .uri(URI.create(ACCESS + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code&code=" + server.getCode() + "&redirect_uri=http://localhost:8080"))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(post, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
}
