package messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import validation.Validatable;

public class PointWithScale {
    public PointWithScale(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Validatable(min = -3, max = 5)
    public final int x;
    @Validatable(min = -5, max = 3)
    public final int y;
    @Validatable(min = 1, max = 5)
    public final int r;

    public static PointWithScale parse(String x, String y, String r) {
        return new PointWithScale(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(r));
    }

    public void addToJson(ObjectNode json) {
        json.put("X", this.x);
        json.put("Y", this.y);
        json.put("R", this.r);
    }

    public static PointWithScale getFromJson(JsonNode vars) {
        return parse(vars.get("X").asText(), vars.get("Y").asText(), vars.get("R").asText());
    }

    @SuppressWarnings("unused")
    public JsonNode toJsonNode() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(this);
    }
}
