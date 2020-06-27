package com.kakao.task.sprinkle.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int status;
    private String message;

    private ErrorResponse(ErrorCode code) {
        this.status = code.getStatus();
        this.message = code.getMessage();
    }

    private ErrorResponse(ApiException e) {
        this.status = e.getErrorCode().getStatus();
        this.message = e.getErrorCode().getMessage();
    }

    public static ErrorResponse of(final ApiException e) {
        return new ErrorResponse(e);
    }
}
