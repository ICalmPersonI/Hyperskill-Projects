package server;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class Session extends Thread  {

    private boolean serverShutdown = false;

    private final Socket socket;

    private final DataBase dataBase;

    Session(Socket serverSocket, DataBase db) {
        this.socket = serverSocket;
        this.dataBase = db;
    }



    @Override
    public void run() {

        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            JSONObject jsonObject = new JSONObject(input.readUTF());

            String type = jsonObject.get("type").toString();

            if (type.matches("exit")) {
                Utils.save(dataBase);
                output.writeUTF("{\"response\":\"OK\"}");
                serverShutdown = true;

            } else {

                String key = jsonObject.get("key").toString();
                JSONObject value = new JSONObject();
                String stringValue = "";
                try {
                    value = jsonObject.getJSONObject("value");
                } catch (JSONException e) {
                    stringValue = jsonObject.optString("value");
                }

                Map<String, String> sent = new LinkedHashMap<>();

                try {
                    JSONArray keys;
                    try {
                        keys = new JSONArray(key);
                    } catch (JSONException exception) {
                        keys = new JSONArray("[" + key + "]");
                    }
                    JSONObject takenValue;
                    try {
                        takenValue = dataBase.get(keys.get(0).toString());
                    } catch (NullPointerException e) {
                        takenValue = null;
                    }
                    switch (type) {
                        case "get":
                            if (keys.length() > 1) {
                                String elem = takenValue.get(keys.get(1).toString()).toString();
                                sent.put("response", "OK");
                                sent.put("value", elem);
                            } else if (keys.length() == 1) {
                                sent.put("response", "OK");
                                sent.put("value", takenValue.toString());
                            } else {
                                sent.put("response", "ERROR");
                                sent.put("reason", "No such key");
                            }
                            break;
                        case "set":
                            if (keys.length() > 1) {
                                dataBase.set(
                                        updateJson(takenValue, keys.get(keys.length() - 1).toString(), stringValue),
                                        keys.get(0).toString());
                            } else {
                                dataBase.set(value, key);
                            }
                            sent.put("response", "OK");
                            break;
                        case "delete":
                            if (keys.length() > 1) {
                                dataBase.set(
                                        removeJson(takenValue, keys.get(keys.length() - 1).toString()),
                                        keys.get(0).toString());
                                sent.put("response", "OK");
                            } else if (dataBase.delete(key)) {
                                sent.put("response", "OK");
                            } else {
                                sent.put("response", "ERROR");
                                sent.put("reason", "No such key");
                            }
                            break;
                        case "exit":
                            sent.put("response", "OK");
                            System.exit(0);
                        default:
                            sent.put("response", "ERROR");
                            sent.put("reason", "No such key");
                    }
                } catch (Exception e) {
                    sent.put("response", "ERROR");
                    sent.put("reason", "No such key");
                    e.printStackTrace();
                }
                output.writeUTF(new Gson().toJson(sent));
            }

        } catch (IOException | JSONException ioException) {
            ioException.printStackTrace();
        }
    }

    //Method by eabyshev, taken at stackoverflow.
    public JSONObject updateJson(JSONObject obj, String keyString, String newValue) throws Exception {
        Iterator iterator = obj.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if ((obj.optJSONArray(key) == null) && (obj.optJSONObject(key) == null)) {
                if ((key.equals(keyString))) {
                    obj.put(key, newValue);
                    return obj;
                }
            }

            if (obj.optJSONObject(key) != null) {
                updateJson(obj.getJSONObject(key), keyString, newValue);
            }

            if (obj.optJSONArray(key) != null) {
                JSONArray jArray = obj.getJSONArray(key);
                for (int i = 0; i < jArray.length(); i++) {
                    updateJson(jArray.getJSONObject(i), keyString, newValue);
                }
            }
        }
        return obj;
    }

    public JSONObject removeJson(JSONObject obj, String keyString) throws Exception {
        Iterator iterator = obj.keys();
        String key;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            if ((obj.optJSONArray(key) == null) && (obj.optJSONObject(key) == null)) {
                if ((key.equals(keyString))) {
                    obj.remove(key);
                    return obj;
                }
            }

            if (obj.optJSONObject(key) != null) {
               removeJson(obj.getJSONObject(key), keyString);
            }

            if (obj.optJSONArray(key) != null) {
                JSONArray jArray = obj.getJSONArray(key);
                for (int i = 0; i < jArray.length(); i++) {
                    removeJson(jArray.getJSONObject(i), keyString);
                }
            }
        }
        return obj;
    }


    public boolean isServerShutdown() {
        return serverShutdown;
    }

}
