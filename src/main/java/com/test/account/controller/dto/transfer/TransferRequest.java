package com.test.account.controller.dto.transfer;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TransferRequest(
        @NotNull(message = "fromAccount는 필수입니다")
        String fromAccountNumber,
        @NotNull(message = "toAccount는 필수입니다")
        String toAccountNumber,
        @Min(value = 1, message = "amount는 1원이상이어야 합니다")
        long amount
) {}