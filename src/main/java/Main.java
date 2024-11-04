import com.fastcgi.FCGIInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import messaging.*;
import validation.Validator;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        var fsgiInterface = new FCGIInterface();
        while (fsgiInterface.FCGIaccept() >= 0) {
            Properties params = FCGIInterface.request.params;
            if (Objects.equals(params.getProperty("REQUEST_METHOD"), "POST")) {
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

    public static void post() throws BadRequestException {

        String queryString;
        try {
            queryString = Sender.readRequestBody();
        } catch (IOException e) {
            throw new BadRequestException();
        }
        JsonNode vars;
        try {
            vars = JsonManager.getJson(queryString);
        } catch (JsonProcessingException e) {
            throw new BadRequestException();
        }

        try {
            PointWithScale point = PointWithScale.getFromJson(vars);
            if (Validator.validate(point)) {
                throw new BadRequestException();
            }
            if (Validator.check(point)) {
                JsonNode jsonNode = JsonManager.makeJsonToSend("true", point);
                Sender.send(HttpStatus.OK, jsonNode);
            } else {
                JsonNode jsonNode = JsonManager.makeJsonToSend("false", point);
                Sender.send(HttpStatus.OK, jsonNode);
            }
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new BadRequestException();
        }
    }

    public static void notImplemented() {
        Sender.send(HttpStatus.NOT_IMPLEMENTED);
    }
}
