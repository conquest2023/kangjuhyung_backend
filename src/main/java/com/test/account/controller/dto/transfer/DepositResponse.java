package com.test.account.controller.dto.transfer;

import com.test.account.service.domain.Cashier;
import com.test.account.service.domain.enumtype.TransactionDirection;
import com.test.account.service.domain.enumtype.TransactionStatus;
import com.test.account.service.domain.enumtype.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DepositResponse(
        BigDecimal depositedAmount,
        BigDecimal balanceBefore,
        BigDecimal balanceAfter,
        LocalDateTime transactionAt
) {
    public static DepositResponse from(Cashier cashier) {
        return new DepositResponse(
                cashier.getAmount(),
                cashier.getBalanceBefore(),
                cashier.getBalanceAfter(),
                cashier.getProcessedAt()
        );
    }
}
