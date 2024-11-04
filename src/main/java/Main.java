import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import messaging.BadRequestException;
import messaging.HttpStatus;
import messaging.Sender;
import validation.Validator;

public class Main {
    public static void main(String[] args) {
        var fsgiInterface = new FCGIInterface();
        while (fsgiInterface.FCGIaccept() >= 0) {
            Properties params = FCGIInterface.request.params;
            if(Objects.equals(params.getProperty("REQUEST_METHOD"), "POST")) {
                try {
                    post();
                } catch (BadRequestException e) {
                    Sender.send(HttpStatus.BAD_REQUEST);
                }
            } else {
                notImplemented();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void post() throws BadRequestException{

        String queryString;
        try {
            queryString = Sender.readRequestBody();
        } catch (IOException e) {
            throw new BadRequestException();
        }
        HashMap<String, Object> vars;
        try {
            vars = new ObjectMapper().readValue(queryString, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new BadRequestException();
        }

        try {
            int x = Integer.parseInt((String) vars.get("X"));
            int y = Integer.parseInt((String) vars.get("Y"));
            int R = Integer.parseInt((String) vars.get("R"));
            if(Validator.validate(x, y, R)) {
                throw new BadRequestException();
            }
            if (Validator.check(x, y, R)) {
                Sender.send(HttpStatus.OK, "true", x, y, R);
            } else {
                Sender.send(HttpStatus.OK, "false", x, y, R);
            }
        } catch (NoSuchElementException | NumberFormatException e){
            throw new BadRequestException();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void notImplemented() {
        Sender.send(HttpStatus.NOT_IMPLEMENTED);
    }
}
