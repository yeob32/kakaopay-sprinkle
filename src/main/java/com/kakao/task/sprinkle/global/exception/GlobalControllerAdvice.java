package com.kakao.task.sprinkle.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final ApiException e) {
        log.error("handle API Exception > ", e);
        return new ResponseEntity<>(ErrorResponse.of(e), HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }
}
