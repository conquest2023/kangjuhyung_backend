package com.test.account.exception;

import com.test.account.controller.advice.ErrorCode;

public class InvalidAmountException extends BusinessException {
    public InvalidAmountException() {
        super(ErrorCode.INVALID_AMOUNT);
    }
}