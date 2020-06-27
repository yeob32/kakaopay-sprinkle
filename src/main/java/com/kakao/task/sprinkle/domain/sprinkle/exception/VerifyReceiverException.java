package com.kakao.task.sprinkle.domain.sprinkle.exception;

import com.kakao.task.sprinkle.global.exception.ApiException;
import com.kakao.task.sprinkle.global.exception.ErrorCode;

public class VerifyReceiverException extends ApiException {
    public VerifyReceiverException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public VerifyReceiverException(ErrorCode errorCode) {
        super(errorCode);
    }
}
