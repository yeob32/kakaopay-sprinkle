package com.kakao.task.sprinkle.domain.sprinkle.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
