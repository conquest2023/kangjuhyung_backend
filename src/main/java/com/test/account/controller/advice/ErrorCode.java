package com.test.account.controller.advice;


import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "요청한 경로를 찾을 수 없습니다"),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "지원하지 않는 HTTP 메서드입니다"),

    SELF_TRANSFER_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "SELF_TRANSFER_NOT_ALLOWED", "출금 계좌와 입금 계좌가 동일할 수 없습니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT_NOT_FOUND", "계좌가 존재하지 않습니다."),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "INVALID_AMOUNT", "금액은 0보다 커야 합니다."),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "INSUFFICIENT_BALANCE", "잔액이 부족합니다."),
    DAILY_WITHDRAW_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "DAILY_WITHDRAW_LIMIT_EXCEEDED", "오늘 하루 출금 한도를 초과했습니다."),
    DAILY_TRANSFER_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "DAILY_TRANSFER_LIMIT_EXCEEDED", "오늘 하루 이체 한도를 초과했습니다."),
    ACCOUNT_FORBIDDEN(HttpStatus.FORBIDDEN, "ACCOUNT_FORBIDDEN", "해당 계좌에 대한 권한이 없습니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "요청 값이 올바르지 않습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String code, String defaultMessage) {
        this.status = status;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus status() { return status; }
    public String code() { return code; }
    public String defaultMessage() { return defaultMessage; }
}
