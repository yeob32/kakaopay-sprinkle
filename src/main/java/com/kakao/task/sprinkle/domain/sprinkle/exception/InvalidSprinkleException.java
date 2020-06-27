package com.kakao.task.sprinkle.domain.sprinkle.exception;

import com.kakao.task.sprinkle.global.exception.ApiException;
import com.kakao.task.sprinkle.global.exception.ErrorCode;

public class InvalidSprinkleException extends ApiException {
    public InvalidSprinkleException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InvalidSprinkleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
