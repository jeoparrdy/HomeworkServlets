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
            writer.println("GET /evening?name="+name+" HTTP/1.1");
            writer.println("Host: " + InetAddress.getLocalHost().getHostAddress());
            //writer.println("Cookie: JSESSIONID=80618367C4B68BD99C805C57D9BC5CE0");
            writer.println();
            writer.flush();
            reader.lines().forEach(System.out::println);
        }
    }

}
