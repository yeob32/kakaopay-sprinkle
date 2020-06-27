package com.kakao.task.sprinkle.domain.sprinkle.exception;

import com.kakao.task.sprinkle.global.exception.ApiException;
import com.kakao.task.sprinkle.global.exception.ErrorCode;

public class ExpiredSprinkleException extends ApiException {

    public ExpiredSprinkleException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ExpiredSprinkleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
