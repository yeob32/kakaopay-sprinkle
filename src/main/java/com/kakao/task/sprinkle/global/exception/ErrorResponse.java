package com.kakao.task.sprinkle.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int status;
    private String message;
    private String description;

    private ErrorResponse(ErrorCode code, String description) {
        this.status = code.getStatus();
        this.message = code.getMessage();
        this.description = description;
    }

    private ErrorResponse(ApiException e) {
        this.status = e.getErrorCode().getStatus();
        this.message = e.getErrorCode().getMessage();
    }

    public static ErrorResponse of(final ApiException e) {
        return new ErrorResponse(e);
    }

    public static ErrorResponse of(final ErrorCode errorCode, String description) {
        return new ErrorResponse(errorCode, description);
    }
}
