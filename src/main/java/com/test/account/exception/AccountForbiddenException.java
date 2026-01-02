package com.test.account.exception;

import com.test.account.controller.advice.ErrorCode;

public class AccountForbiddenException extends BusinessException {
    public AccountForbiddenException() {
        super(ErrorCode.ACCOUNT_FORBIDDEN);
    }
}