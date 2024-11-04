package messaging;

public enum HttpStatus {
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_IMPLEMENTED(501, "Not Implemented");

    private final int code;
    private final String description;

    HttpStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.code + " " + this.description;
    }
}