package messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonManager {
    public static final long startTime = System.nanoTime();

    public static JsonNode getJson(String jsonStr) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonStr);
    }

    public static String makeString(JsonNode jsonNode) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
    }

    public static JsonNode makeJsonToSend(String content, PointWithScale point) {
        LocalDateTime time = java.time.LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String timeString = time.format(formatter);
        String duration = String.valueOf((System.nanoTime() - startTime) / 1_000);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();
        json.put("time", timeString);
        json.put("duration", duration);
        json.put("content", content);
        point.addToJson(json);
        return json;
    }
}
