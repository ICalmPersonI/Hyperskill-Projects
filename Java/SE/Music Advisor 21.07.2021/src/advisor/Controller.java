package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.*;

public class Controller {

    private final Model model;
    private final View view;

    Controller(Model model, View view) {
        this.model = model;
        this.view = view;

    }

    private void parsePlaylistCategory(String string) {
        boolean unknownCategory = true;
        if (string.matches("playlists [\\w\\s]+")) {
            String category = string
                    .replace("playlists ", "")
                    .toLowerCase();
            for (JsonElement elem : model.getCategories()) {
                if (elem.getAsJsonObject().get("name").toString().toLowerCase()
                        .replace("\"", "").matches(category)) {
                     view.addToQueue(parseJsonPlaylistByCategory(elem.getAsJsonObject().get("id")
                           .toString().replace("\"", "")));
                    unknownCategory = false;
                    break;
                }
            }
        }
        if (unknownCategory)
            System.out.println("Unknown category name.");
    }

    public void updateView(String choice) {
        switch (choice) {
            case "new":
                view.addToQueue(parseJsonNewRelease());
                break;
            case "featured":
                view.addToQueue(parseJsonFeatured());
                break;
            case "categories":
                view.addToQueue(parseJsonCategories());
                break;
            case "next":
                view.next();
                break;
            case "prev":
                view.prev();
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                parsePlaylistCategory(choice);
        }

    }

    private List<String> parseJsonNewRelease() {
        List<String> list = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        model.getNewRelease().forEach(elem -> {
            stringBuilder.append(elem.getAsJsonObject().get("name")).append("\n").
                    append("[");
            elem.getAsJsonObject().get("artists").getAsJsonArray().forEach(e ->
                    stringBuilder.append(e.getAsJsonObject()
                            .get("name")
                            .toString())
                            .append(", "));
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append("]").append("\n")
                    .append(elem.getAsJsonObject()
                            .get("external_urls")
                            .getAsJsonObject()
                            .get("spotify"))
                    .append("\n");
            list.add(stringBuilder.toString().replace("\"", ""));
            stringBuilder.setLength(0);
        });
        return list;
    }

    private List<String> parseJsonFeatured() {
        List<String> list = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        model.getFeaturedPlaylists().forEach(elem -> {
            stringBuilder.append(elem
                    .getAsJsonObject()
                    .get("name")
                    .toString())
                    .append("\n")
                    .append(elem
                            .getAsJsonObject()
                            .get("external_urls")
                            .getAsJsonObject()
                            .get("spotify")
                            .toString()).append("\n");
            list.add(stringBuilder.toString().replace("\"", ""));
            stringBuilder.setLength(0);
        });
        return list;
    }

    private List<String> parseJsonCategories() {
        List<String> list = new ArrayList<>();
        model.getCategories().forEach(elem -> {
            list.add(elem
                    .getAsJsonObject()
                    .get("name")
                    .toString()
                    .replace("\"", ""));
        });
        return list;
    }

    private List<String> parseJsonPlaylistByCategory(String category) {
        List<String> list = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        Optional<JsonArray> playlist = Optional
                .ofNullable(model.getPlayListByCategory(category));
        if (playlist.isPresent()) {
            playlist.get().forEach(elem -> {
                stringBuilder.append(elem
                        .getAsJsonObject()
                        .get("name")
                        .toString())
                        .append("\n")
                        .append(elem
                                .getAsJsonObject()
                                .get("external_urls")
                                .getAsJsonObject()
                                .get("spotify")
                                .toString()).append("\n");
                list.add(stringBuilder.toString().replace("\"", ""));
                stringBuilder.setLength(0);
            });
            return list;
        }
        return list;
    }
}
