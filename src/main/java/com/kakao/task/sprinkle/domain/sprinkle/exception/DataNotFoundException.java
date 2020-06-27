package com.kakao.task.sprinkle.domain.sprinkle.exception;

import com.kakao.task.sprinkle.global.exception.ApiException;
import com.kakao.task.sprinkle.global.exception.ErrorCode;

public class DataNotFoundException extends ApiException {

    public DataNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
