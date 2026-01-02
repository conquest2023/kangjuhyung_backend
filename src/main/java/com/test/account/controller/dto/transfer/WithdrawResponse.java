package com.test.account.controller.dto.transfer;

import com.test.account.service.domain.Cashier;
import com.test.account.service.record.WithdrawResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WithdrawResponse(
        BigDecimal withdrawnAmount,
        BigDecimal balanceBefore,
        BigDecimal balanceAfter,
        LocalDateTime transactionAt,
        long todayWithdrawTotal,

        long todayWithdrawRemaining
) {
    public static WithdrawResponse from(WithdrawResult r) {
        Cashier c = r.cashier();
        long remaining = Math.max(0, 1000000 - r.todayWithdrawTotal());
        return new WithdrawResponse(
                c.getAmount(),
                c.getBalanceBefore(),
                c.getBalanceAfter(),
                c.getProcessedAt(),
                r.todayWithdrawTotal(),
                remaining
        );
    }
}
