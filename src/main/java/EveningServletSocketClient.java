import lombok.SneakyThrows;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class EveningServletSocketClient {
    @SneakyThrows
    public static void main(String[] args) {
        try (var socket = new Socket(InetAddress.getLocalHost().getHostAddress(),8080);
            var writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            String name = "Serhii%20Feshchuk";
            writer.println("GET /evening HTTP/1.1");
            writer.println("Host: " + InetAddress.getLocalHost().getHostAddress());
            writer.println("Cookie: JSESSIONID=BFD3C4F0B5EC4E47B5F0D97B992DD1F4");
            writer.println();
            writer.flush();
            reader.lines().forEach(System.out::println);
        }
    }

}
