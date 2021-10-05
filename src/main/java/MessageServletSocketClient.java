import lombok.SneakyThrows;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MessageServletSocketClient {
    @SneakyThrows
    public static void main(String[] args) {
        try (var socket = new Socket(InetAddress.getLocalHost().getHostAddress(),8080);
             var writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF8"));
             var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))){

            JSONObject jo = new JSONObject();
            jo.put("name", "Serhii Feshchuk");
            jo.put("message", "This is crazy!!!");

            var jsonText =  jo.toString();
            System.out.println(jsonText);

            writer.println("POST /message HTTP/1.1");
            writer.println("Host: " + InetAddress.getLocalHost().getHostAddress());
            writer.println("Content-Type: application/json");
            writer.println("Content-Length: " + jsonText.length());
            writer.println();
            writer.println(jsonText);
            writer.flush();

            reader.lines().forEach(System.out::println);
        }
    }
}
