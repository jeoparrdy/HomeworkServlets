import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ZalikSocketClient {
    @SneakyThrows
    public static void main(String[] args) {
        var socket = new Socket("93.175.204.87", 8080);
        var writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        JSONObject jo = new JSONObject();
        jo.put("firstName", " ");
        jo.put("lastName", " ");

        var jsonText =  jo.toString();
        System.out.println(jsonText);

        writer.println("POST /ping HTTP/1.1");
        writer.println("Host: 93.175.204.87");
        writer.println("Content-Type: application/json");
        writer.println("Content-Length: " + jsonText.length());
        writer.println();
        writer.println(jsonText);
        writer.flush();

        reader.lines().forEach(System.out::println);
    }
}
