package com.test.account.mapper;

import com.test.account.infrastructure.entity.AccountEntity;
import com.test.account.infrastructure.entity.AccountTransactionEntity;
import com.test.account.service.domain.Cashier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountTransactionMapper {


    public static AccountTransactionEntity toEntity(
            Cashier log,
            AccountEntity accountRef
    ) {
        return AccountTransactionEntity.builder()
                .account(accountRef)
                .counterpartyAccountId(log.getCounterpartyAccountId())
//                .txGroupId(log.getTxGroupId())
                .type(log.getTransactionType())
                .status(log.getStatus())
                .amount(log.getAmount())
//                .feeAmount(
//                        log.getFeeAmount() != null ? log.getFeeAmount() : BigDecimal.ZERO
//                )
                .direction(log.getDirection())
                .balanceBefore(log.getBalanceBefore())
                .balanceAfter(log.getBalanceAfter())
//                .idempotencyKey(log.get)
//                .counterpartyName(log.getCounterpartyName())
                .createdAt(LocalDateTime.now())
                .postedAt(log.getProcessedAt())
//                .reversedTx(null)
                .build();
    }
    public static Cashier toDomain(AccountTransactionEntity e) {

        return Cashier.builder()
                .accountId(e.getAccount().getAccountId())
                .transactionId(e.getTxId())
                .transactionType(e.getType())
                .status(e.getStatus())
                .amount(e.getAmount())
                .direction(e.getDirection())
                .balanceBefore(e.getBalanceBefore())
                .balanceAfter(e.getBalanceAfter())
                .processedAt(e.getPostedAt())
                .counterpartyAccountId(e.getCounterpartyAccountId())
                .build();
    }
}
