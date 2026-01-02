package com.test.account.controller.dto.transfer;

import com.test.account.service.domain.Cashier;
import com.test.account.service.domain.enumtype.TransactionDirection;
import com.test.account.service.domain.enumtype.TransactionStatus;
import com.test.account.service.record.TransferResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(
        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal transferAmount,
        BigDecimal balanceAfterTransfer,
        long remainingLimit,
        TransactionStatus status,

        LocalDateTime transactionAt
) {
    public static TransferResponse from(
            TransferResult result,
            String fromAccountNumber,
            String toAccountNumber
    ) {
        long remaining = Math.max(0, 3000000 -  result.todayTransferTotal());
        return new TransferResponse(
                fromAccountNumber,
                toAccountNumber,
                result.cashier().getAmount(),
                result.cashier().getBalanceAfter(),
                remaining,
                result.cashier().getStatus(),
                result.cashier().getProcessedAt()
        );
    }
}