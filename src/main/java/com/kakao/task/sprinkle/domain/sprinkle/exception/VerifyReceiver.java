package com.kakao.task.sprinkle.domain.sprinkle.exception;

import com.kakao.task.sprinkle.global.exception.ApiException;
import com.kakao.task.sprinkle.global.exception.ErrorCode;

public class VerifyReceiver extends ApiException {
    public VerifyReceiver(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public VerifyReceiver(ErrorCode errorCode) {
        super(errorCode);
    }
}
