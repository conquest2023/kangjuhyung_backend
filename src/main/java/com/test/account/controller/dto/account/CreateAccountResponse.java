package com.test.account.controller.dto.account;

import com.test.account.service.domain.Account;
import com.test.account.service.domain.enumtype.AccountStatus;
import com.test.account.service.domain.enumtype.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateAccountResponse(
        String accountNumber,
        AccountType accountType,
        BigDecimal balance,
        AccountStatus status,
        LocalDateTime createdAt
) {
    public static CreateAccountResponse from(Account account) {
        return new CreateAccountResponse(
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt()
        );
    }
}
