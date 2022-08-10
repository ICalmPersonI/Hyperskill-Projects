package antifraud;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class Util {
    public static final HttpHeaders JSON_HEADERS = new HttpHeaders();

    static {
        JSON_HEADERS.setContentType(MediaType.APPLICATION_JSON);
    }
}
