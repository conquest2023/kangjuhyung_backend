package com.test.account.service.domain;

import com.test.account.service.domain.enumtype.TransactionDirection;
import com.test.account.service.domain.enumtype.TransactionStatus;
import com.test.account.service.domain.enumtype.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.management.LockInfo;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountHistory {


    private Long accountId;

    private Long txId;
    private Account account;
    private Long counterpartyAccountId;
    private TransactionStatus status;
    private TransactionType type;

    private TransactionDirection direction;
    private BigDecimal amount;

    private BigDecimal feeAmount;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;


    private String counterpartyName;

    private LocalDateTime createdAt;

    private LocalDateTime postedAt;


}
