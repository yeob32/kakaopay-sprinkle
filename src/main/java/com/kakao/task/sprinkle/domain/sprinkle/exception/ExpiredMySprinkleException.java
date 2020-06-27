package com.kakao.task.sprinkle.domain.sprinkle.exception;

import com.kakao.task.sprinkle.global.exception.ApiException;
import com.kakao.task.sprinkle.global.exception.ErrorCode;

public class ExpiredMySprinkleException extends ApiException {
    public ExpiredMySprinkleException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ExpiredMySprinkleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
