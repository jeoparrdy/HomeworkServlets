import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.*;

public class BiggestPhoto {

    public static void main(String[] args) {

        Map<String, Integer> imgSizeMap = photoInfo(getImagesLinks());

        Optional<Map.Entry<String, Integer>> maxEntry = imgSizeMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        var biggestPhotoUrl = maxEntry.get().getKey();
        var size = maxEntry.get().getValue();
        var jsonResponse = prepareResponseInJasonFormat(biggestPhotoUrl,size);
        System.out.println(jsonResponse);
        sendThisShitToBobocodeServer(jsonResponse);
    }


    @SneakyThrows
    private static List<String> getImagesLinks(){
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
                        .map(JsonNode::asText)
                        .collect(Collectors.toList());

        return imgUrlList;
    }

    public static Map<String,Integer> photoInfo(List<String> urlList){
        Map<String,Integer> biggestPhoto = new HashMap<>();
        for (String urlString: urlList) {
           // System.out.println(urlString + " -> " + receiveImageSize(urlString));
            biggestPhoto.put(urlString, receiveImageSize(urlString));
        }
        return biggestPhoto;
    }

    private static int receiveImageSize(String urlString){
        var location = getHeader(urlString, "Location");
        var photo = getHeader(location, "Content-Length");
        return Integer.parseInt(photo);
    }

    @SneakyThrows
    private static String getHeader(String link, String header){
        //link.replace("\"", "");
        //System.out.println(link);
        var uri = URI.create(link);

        var socket = new Socket(uri.getHost(), 80);
        var printer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printer.println("HEAD " + uri.getPath() + " HTTP/1.1");
        printer.println("Host: " + uri.getHost() +":80");
        printer.println();
        printer.flush();
       // reader.lines().forEach(System.out::println);

        String imgLengthString = reader.lines()
                .filter(answer -> answer.startsWith(header))
                .map(answer -> answer.split(" ")[1].trim())
                .findFirst()
                .orElse("Empty file or wrong answer!");
        return imgLengthString;
    }

    @SneakyThrows
    private static void sendThisShitToBobocodeServer(String response) {
        URI uri = URI.create("https://bobocode.herokuapp.com/nasa/pictures");
        var factory = (SSLSocketFactory) SSLSocketFactory.getDefault(); //https://bobocode.herokuapp.com/nasa/pictures
        var socket = (SSLSocket) factory.createSocket(uri.getHost(), 443);
        var printer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        printer.println("POST " + uri.getPath() + " HTTP/1.1\r\n");
        printer.println("Host: " + uri.getHost()+"\r\n");
        printer.println("Content-type: application/json\r\n");
        printer.println("Content.length: " + response.length()+"\r\n");
        printer.println("\r\n");
        printer.println(response+"\r\n");
        printer.println("\r\n");
        printer.flush();
        String strResp = reader.lines().collect(Collectors.joining("\r\n"));
        System.out.println(strResp);

    }

    @SneakyThrows
    private static String prepareResponseInJasonFormat(String biggestPhotoUrl, Integer size){
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode imgNode = mapper.createObjectNode();
        imgNode.put("url", biggestPhotoUrl);
        imgNode.put("size", size);

        ObjectNode userNode = mapper.createObjectNode();
        userNode.put("firstName","Serhii");
        userNode.put("lastName","Feshchuk");

        ObjectNode resultNode = mapper.createObjectNode();
        resultNode.set("picture", imgNode);
        resultNode.set("user", userNode);

        return mapper.writeValueAsString(resultNode);
    }


}
