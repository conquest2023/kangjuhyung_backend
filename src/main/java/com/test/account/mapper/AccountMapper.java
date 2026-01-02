package com.test.account.mapper;

import com.test.account.infrastructure.entity.AccountEntity;
import com.test.account.service.domain.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public static AccountEntity toEntity(Account d) {
        if (d == null) return null;

        return AccountEntity.builder()
                .accountId(d.getAccountId())
                .accountNumber(d.getAccountNumber())
                .balance(d.getBalance())
                .status(d.getStatus())
                .accountType(d.getAccountType())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .deletedAt(d.getDeletedAt())
                .lastTransactionAt(d.getLastTransactionAt())
                .build();
    }

    public static Account toDomain(AccountEntity e) {
        if (e == null) return null;

        return Account.builder()
                .accountId(e.getAccountId())
                .accountNumber(e.getAccountNumber())
                .balance(e.getBalance())
                .status(e.getStatus())
                .accountType(e.getAccountType())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .lastTransactionAt(e.getLastTransactionAt())
                .build();
    }
}
