package com.kakao.task.sprinkle.domain.sprinkle.exception;

public class ChatNotFoundException extends RuntimeException {

    public ChatNotFoundException(String message) {
        super(message);
    }
}
