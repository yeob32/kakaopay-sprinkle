package com.kakao.task.sprinkle.global.exception;

public enum ErrorCode {

    USER_NOT_FOUND(404, "Not Found User"),
    CHAT_NOT_FOUND(404, "Not Found Chat Room"),
    SPRINKLE_NOT_FOUND(404, "Not Found Sprinkle"),
    VERIFY_RECEIVER(403, "invaild receiver"),
    SPRINKLE_CLOSE(403, "Sprinkle is Over"),
    RETREIVE_EXPIRED(403, "Expired Sprinkle"),
    RECEIVE_DUPLICATION(403, "Receive Duplication"),
    RECEIVE_EXPIRED(400, "Expired Receive"),
    REQUIRED_PARAMETER(400, "Required parameter"),
    INVALID_CHATTER(401, "Invalid Chat Member"),
    INVALID_SPRINKLE(401, "Invalid Sprinkle"),
    INVALID_INPUT_VALUE(400, "Invalid input value");


    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return this.message;
    }
}
