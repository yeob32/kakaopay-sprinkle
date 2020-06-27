package com.kakao.task.sprinkle.domain.chat;

import com.kakao.task.sprinkle.global.exception.ApiException;
import com.kakao.task.sprinkle.global.exception.ErrorCode;

public class InvalidChatterException extends ApiException {
    public InvalidChatterException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InvalidChatterException(ErrorCode errorCode) {
        super(errorCode);
    }
}
