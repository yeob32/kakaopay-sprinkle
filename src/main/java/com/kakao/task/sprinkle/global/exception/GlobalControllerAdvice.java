package com.kakao.task.sprinkle.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final ApiException e) {
        log.error("handle API Exception > ", e);
        return new ResponseEntity<>(ErrorResponse.of(e), HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException e) {
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.REQUIRED_PARAMETER, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleBindException(BindException e) {
        return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getMessage());
    }
}
