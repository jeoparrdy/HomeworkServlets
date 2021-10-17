import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.*;

public class BiggestPhoto {

    public static void main(String[] args) {
        biggestPhotoInfo(getImagesLinks());
    }

    @SneakyThrows
    public static List<String> getImagesLinks(){
        var factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        var socket = (SSLSocket) factory.createSocket("api.nasa.gov", 443);
        var printer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        printer.println("GET https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=12&api_key=DEMO_KEY HTTP/1.0");
        printer.println("Host: api.nasa.gov");
        printer.println();
        printer.flush();

        var jsonResp = reader.lines().filter(s->s.startsWith("{")).findFirst().orElseThrow();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readValue(jsonResp, JsonNode.class);
        List<String> imgUrlList = StreamSupport.stream(jsonNode.get("photos").spliterator(),false)
                        .map(json -> json.get("img_src"))
                        .map(JsonNode::toString)
                        .collect(Collectors.toList());
        return imgUrlList;
    }

    public static Map<String,String> biggestPhotoInfo(List<String> urlList){
        Map<String,String> biggestPhoto = new HashMap<>();


        return null;
    }

    @SneakyThrows
    public static String receiveImageSize(String link){
        var factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        var socket = (SSLSocket) factory.createSocket("api.nasa.gov", 443);
        var printer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        return null;
    }
}
