package com.kakao.task.sprinkle.domain.sprinkle.exception;

import com.kakao.task.sprinkle.global.exception.ApiException;
import com.kakao.task.sprinkle.global.exception.ErrorCode;

public class DuplicateReceiveException extends ApiException {
    public DuplicateReceiveException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public DuplicateReceiveException(ErrorCode errorCode) {
        super(errorCode);
    }
}
