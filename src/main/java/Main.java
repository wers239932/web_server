import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import validation.Validate;

public class Main {
    public static void main(String[] args) {
        var fsgiInterface = new FCGIInterface();
        while (fsgiInterface.FCGIaccept() >= 0) {
            Properties params = FCGIInterface.request.params;
            switch (params.getProperty("REQUEST_METHOD")) {
                case ("GET") -> get();
                case ("POST") -> post();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Validate(minX = -3, maxX = 5, minY = -5, maxY = -3, minR = 1, maxR = 5)
    public static void post() {

        String queryString;
        try {
            queryString = Sender.readRequestBody();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> vars = new HashMap<>();
        try {
            vars = new ObjectMapper().readValue(queryString, HashMap.class);
        } catch (JsonProcessingException e) {
            Sender.send(HttpStatus.BAD_REQUEST);
        }

        try {
            int x = Integer.parseInt((String) vars.get("X"));
            int y = Integer.parseInt((String) vars.get("Y"));
            int R = Integer.parseInt((String) vars.get("R"));
            if(validate(x,y,R)) {
                Sender.send(HttpStatus.BAD_REQUEST);
            }
            if (check(x, y, R)) {
                Sender.send(HttpStatus.OK, "true", x, y, R);
            } else {
                Sender.send(HttpStatus.OK, "false", x, y, R);
            }
        } catch (NoSuchElementException | NumberFormatException e){
            Sender.send(HttpStatus.BAD_REQUEST);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean check(Integer x, Integer y, Integer R) {
        if (x >= 0 && y >= 0) return x <= R && y <= R / 2;
        if (x >= 0 && y < 0) return (x + y) * 2 <= R * 3;
        if (x < 0 && y < 0) return x * x + y * y <= R * R;
        return false;
    }

    public static void get() {
        Sender.send(HttpStatus.NOT_IMPLEMENTED);
    }

    @Validate(minX = -3, maxX = 5, minY = -5, maxY = -3, minR = 1, maxR = 5)
    public static boolean validate(int x, int y, int R) throws NoSuchMethodException {
        Validate annotation = Main.class.getMethod("validate").getAnnotation(Validate.class);
        int minX = annotation.minX();
        int minY = annotation.minY();
        int minR = annotation.minR();
        int maxX = annotation.maxX();
        int maxY = annotation.maxY();
        int maxR = annotation.maxR();
        return !(x<minX || x>maxX || y<minY || y>maxY || R<minR || R>maxR);
    }
}
