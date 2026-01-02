package com.test.account.controller.dto.error;

import com.test.account.controller.advice.ErrorCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String code,
        String message,
        String path,
        Map<String, String> fields
) {
    public static ErrorResponse of(ErrorCode ec, String message, String path, Map<String, String> fields) {
        return new ErrorResponse(
                LocalDateTime.now().toString(),
                ec.status().value(),
                ec.status().getReasonPhrase(),
                ec.code(),
                message,
                path,
                fields
        );
    }
}