package messaging;

import com.fastcgi.FCGIInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Sender {

    public static void send(HttpStatus ansCode, JsonNode jsonContent) {

        String jsonString;
        try {
            jsonString = JsonManager.makeString(jsonContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var httpResponse = """
                HTTP/1.1 %s
                Content-Type: text/html
                Content-Length: %d
                        
                %s
                """.formatted(String.valueOf(ansCode.getCode()), jsonString.getBytes(StandardCharsets.UTF_8).length, jsonString);
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
