package com.test.account.exception;

import com.test.account.controller.advice.ErrorCode;

public class DailyWithdrawLimitExceededException extends BusinessException {
    public DailyWithdrawLimitExceededException(String message) {
        super(ErrorCode.DAILY_WITHDRAW_LIMIT_EXCEEDED, message);
    }

    public DailyWithdrawLimitExceededException() {
        super(ErrorCode.DAILY_WITHDRAW_LIMIT_EXCEEDED);
    }
}