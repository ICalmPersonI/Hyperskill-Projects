package server;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataBase implements Serializable {
    private volatile Map<String, String> date = new LinkedHashMap<>();

    public synchronized void set(JSONObject value, String key) {
        date.put(key, value.toString());
    }

    public synchronized JSONObject get(String key) throws JSONException {
        return new JSONObject(date.get(key));
    }

    public synchronized boolean delete(String key) {
       return date.remove(key) != null;
    }

}



