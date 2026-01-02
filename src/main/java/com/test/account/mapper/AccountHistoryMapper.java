package com.test.account.mapper;

import com.test.account.infrastructure.entity.AccountTransactionEntity;
import com.test.account.service.domain.AccountHistory;
import com.test.account.service.domain.Cashier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class AccountHistoryMapper {

    private AccountHistoryMapper() {}

    public static AccountHistory toDomain(AccountTransactionEntity e) {
        if (e == null) return null;

        return AccountHistory.builder()
                .accountId(e.getAccount() != null ? e.getAccount().getAccountId() : null)
                .txId(e.getTxId())
                .counterpartyAccountId(e.getCounterpartyAccountId())
                .type(e.getType())
                .status(e.getStatus())
                .direction(e.getDirection())
                .amount(nvl(e.getAmount()))
                .balanceBefore(nvl(e.getBalanceBefore()))
                .balanceAfter(nvl(e.getBalanceAfter()))
                .createdAt(e.getCreatedAt())
                .postedAt(e.getPostedAt())
                .build();
    }

    public static AccountHistory toHistoryDomain(Cashier c) {
        if (c == null) return null;

        return AccountHistory.builder()
                .txId(c.getTransactionId())
                .accountId(c.getAccountId())
                .counterpartyAccountId(c.getCounterpartyAccountId())
                .direction(c.getDirection())
                .type(c.getTransactionType())
                .amount(nvl(c.getAmount()))
                .balanceBefore(nvl(c.getBalanceBefore()))
                .balanceAfter(nvl(c.getBalanceAfter()))
                .status(c.getStatus())
                .createdAt(c.getProcessedAt())
                .build();
    }
    private static BigDecimal nvl(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }
}

