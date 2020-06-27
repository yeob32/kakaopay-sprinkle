package com.kakao.task.sprinkle.domain.sprinkle.exception;

import com.kakao.task.sprinkle.global.exception.ApiException;
import com.kakao.task.sprinkle.global.exception.ErrorCode;

public class CloseSprinkleException extends ApiException {
    public CloseSprinkleException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CloseSprinkleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
