import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@WebServlet("/message")
public class MessageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var reader = new BufferedReader(new InputStreamReader(req.getInputStream()));

        var inputJson = reader.lines().collect(Collectors.joining());
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(inputJson,Message.class);

        System.out.println(message);
    }

    @Data
    private static class Message{
        private String name;
        private String message;
    }
}
