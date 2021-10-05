import lombok.SneakyThrows;

import java.net.InetAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class EveningServletHttpClient{
    @SneakyThrows
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        String str = URLEncoder.encode("http://localhost:8080/evening?name=Serhii Feshchuk", StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/evening?name=Serhii%20Feshchuk"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
