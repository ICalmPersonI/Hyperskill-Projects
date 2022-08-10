package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Model {

    private final String resource;
    private final String accessToken;

    private final JsonArray newRelease;
    private final JsonArray featuredPlaylists;
    private final JsonArray categories;

    Model(String resource, String accessToken) {
        this.resource = resource;
        this.accessToken = accessToken;
        this.newRelease = load("/v1/browse/new-releases", "albums");
        this.featuredPlaylists = load("/v1/browse/featured-playlists", "playlists");
        this.categories = load("/v1/browse/categories", "categories");
    }

    public JsonArray getNewRelease() {
        return newRelease;
    }

    public JsonArray getCategories() {
        return categories;
    }

    public JsonArray getFeaturedPlaylists() {
        return featuredPlaylists;
    }

    public JsonArray getPlayListByCategory(String category) {
        return load("/v1/browse/categories/" + category + "/playlists", "playlists");
    }

    private JsonArray load(String path, String root) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", " Bearer " + accessToken)
                .uri(URI.create(resource + path))
                .build();

        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            try {
                return JsonParser.parseString(response.body())
                        .getAsJsonObject()
                        .get(root).getAsJsonObject().get("items").getAsJsonArray();

            } catch (NullPointerException npe) {
                System.out.println(JsonParser.parseString(response.body())
                        .getAsJsonObject()
                        .get("error")
                        .getAsJsonObject()
                        .get("message"));
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
