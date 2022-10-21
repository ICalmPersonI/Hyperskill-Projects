package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Server {

    private final Handler handler = new Handler();
    private HttpServer server;


    Server() {
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/", handler);
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCode() {
        return handler.getCode();
    }

    public void stop() {
        server.stop(1);
    }


    static class Handler implements HttpHandler {

        private final String UNSUCCESSFULLY = "Authorization code not found. Try again.";
        private final String SUCCESSFULLY = "Got the code. Return back to your program.";

        private String code;


        @Override
        public void handle(HttpExchange t) throws IOException {
            String request = t.getRequestURI().toString();

            OutputStream os = t.getResponseBody();

            if (Pattern.compile("/\\?code=").matcher(request).find()) {
                this.code = request.replace("/?code=", "");
                t.sendResponseHeaders(200, SUCCESSFULLY.length());
                os.write(SUCCESSFULLY.getBytes());

            } else {
                t.sendResponseHeaders(200, UNSUCCESSFULLY.length());
                os.write(UNSUCCESSFULLY.getBytes());
            }

            os.close();
        }

        public String getCode() {
            return code;
        }
    }

}
