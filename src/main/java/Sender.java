import com.fastcgi.FCGIInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sender {
    public static final long startTime = System.nanoTime();
    public static void send(HttpStatus ansCode,String content, Integer x, Integer y, Integer R) {

        LocalDateTime time = java.time.LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String timeString = time.format(formatter);
        String duration = String.valueOf((System.nanoTime()-startTime)/1_000);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();
        json.put("time", timeString);
        json.put("duration", duration);
        json.put("content", content);
        json.put("X", x);
        json.put("Y", y);
        json.put("R", R);
        String jsonString;
        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var httpResponse = """
                HTTP/1.1 %s
                Content-Type: text/html
                Content-Length: %d
                        
                %s
                """.formatted(String.valueOf(ansCode.getCode()),jsonString.getBytes(StandardCharsets.UTF_8).length, jsonString);
        System.out.println(httpResponse);
    }

    public static void send(HttpStatus ansCode) {

        var httpResponse = """
                HTTP/1.1 %s
                Content-Type: text/html
                """.formatted(String.valueOf(ansCode.getCode()));
        System.out.println(httpResponse);
    }

    public static String readRequestBody() throws IOException {
        FCGIInterface.request.inStream.fill();
        var contentLength = FCGIInterface.request.inStream.available();
        var buffer = ByteBuffer.allocate(contentLength);
        var readBytes =
                FCGIInterface.request.inStream.read(buffer.array(), 0,
                        contentLength);
        var requestBodyRaw = new byte[readBytes];
        buffer.get(requestBodyRaw);
        buffer.clear();
        return new String(requestBodyRaw, StandardCharsets.UTF_8);
    }
}
