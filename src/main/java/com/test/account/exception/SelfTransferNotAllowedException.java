package com.test.account.exception;

import com.test.account.controller.advice.ErrorCode;

public class SelfTransferNotAllowedException extends BusinessException{


    public SelfTransferNotAllowedException(String message) {
        super(ErrorCode.SELF_TRANSFER_NOT_ALLOWED, message);
    }

    public SelfTransferNotAllowedException() {

        super(ErrorCode.SELF_TRANSFER_NOT_ALLOWED);
    }
}
