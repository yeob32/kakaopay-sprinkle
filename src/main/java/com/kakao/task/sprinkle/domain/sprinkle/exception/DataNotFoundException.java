package com.kakao.task.sprinkle.domain.sprinkle.exception;

import com.kakao.task.sprinkle.global.exception.ApiException;
import com.kakao.task.sprinkle.global.exception.ErrorCode;

import java.util.UUID;

public class DataNotFoundException extends ApiException {

    public DataNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public DataNotFoundException(long id, ErrorCode errorCode) {
        super(String.format("%s : %s", errorCode.getMessage(), id), errorCode);
    }

    public DataNotFoundException(UUID id, ErrorCode errorCode) {
        super(String.format("%s : %s", errorCode.getMessage(), id), errorCode);
    }
}
