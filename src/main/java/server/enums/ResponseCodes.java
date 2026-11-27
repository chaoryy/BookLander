package server.enums;

public enum ResponseCodes {
    OK(200),
    NOT_FOUND(404),
    SERVER_ERROR(500),
    SEE_OTHER(303),
    BAD_REQUEST(400);

    private final int code;

    ResponseCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
