package com.test.account.controller.dto.transfer;

import jakarta.validation.constraints.Min;

public record WithdrawRequest(String accountNumber,
                              @Min(value = 1, message = "money는 1원이상이어야 합니다")
                              long money) {

}