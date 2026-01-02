package com.test.account.controller.dto.account;

import com.test.account.service.domain.AccountHistory;

public record AccountHistoryResponse(
        Long txId,
        Long accountId,
        Long counterpartyAccountId,
        String type,
        String status,
        String direction,
        String amount,
        String balanceBefore,
        String balanceAfter,
        String createdAt,
        String postedAt
) {
    public static AccountHistoryResponse from(AccountHistory h) {
        return new AccountHistoryResponse(
                h.getTxId(),
                h.getAccountId(),
                h.getCounterpartyAccountId(),
                h.getType().name(),
                h.getStatus().name(),
                h.getDirection().name(),
                h.getAmount().toPlainString(),
//                h.getFeeAmount().toPlainString(),
                h.getBalanceBefore().toPlainString(),
                h.getBalanceAfter().toPlainString(),
                h.getCreatedAt() != null ? h.getCreatedAt().toString() : null,
                h.getPostedAt() != null ? h.getPostedAt().toString() : null
        );
    }
}
