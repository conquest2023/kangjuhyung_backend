package com.test.account.controller.dto.account;

import com.test.account.service.domain.Account;

public record AccountResponse(
        Long accountId,
        String accountNumber,
        String status,
//        String currency,
        String balance
) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getStatus().name(),
//                account.getCurrency(),
                account.getBalance().toPlainString()
        );
    }
}
