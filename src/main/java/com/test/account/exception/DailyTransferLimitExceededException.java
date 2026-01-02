package com.test.account.exception;

import com.test.account.controller.advice.ErrorCode;

public class DailyTransferLimitExceededException extends BusinessException {
    public DailyTransferLimitExceededException(String message) {
        super(ErrorCode.DAILY_TRANSFER_LIMIT_EXCEEDED, message);
    }

    public DailyTransferLimitExceededException() {
        super(ErrorCode.DAILY_TRANSFER_LIMIT_EXCEEDED);
    }
}