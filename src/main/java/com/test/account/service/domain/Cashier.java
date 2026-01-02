package com.test.account.service.domain;

import com.test.account.service.domain.enumtype.TransactionDirection;
import com.test.account.service.domain.enumtype.TransactionStatus;
import com.test.account.service.domain.enumtype.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.test.account.service.domain.enumtype.TransactionType.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cashier {

    private Long transactionId;

    private Long accountId;


    private Long counterpartyAccountId;

    private TransactionDirection direction;

    private TransactionType transactionType;

    private BigDecimal amount;

    private long totalWithdrawLimit;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private TransactionStatus status;

    private LocalDateTime requestedAt;

    private LocalDateTime processedAt;

    public static Cashier depositLog(Long accountId, BigDecimal balance, BigDecimal after, long money, LocalDateTime now){
        return Cashier.builder()
                .accountId(accountId)
                .amount(BigDecimal.valueOf(money))
                .balanceBefore(balance)
                .balanceAfter(after)
                .transactionType(DEPOSIT)
                .status(TransactionStatus.POSTED)
                .direction(TransactionDirection.IN)
                .processedAt(now)
                .build();
    }


    public static Cashier transferInLog(Long fromId,Long toId, BigDecimal balance, long money, BigDecimal after, LocalDateTime now){
        return Cashier.builder()
                .accountId(toId)
                .counterpartyAccountId(fromId)
                .amount(BigDecimal.valueOf(money))
                .balanceBefore(balance)
                .balanceAfter(after)
                .transactionType(TRANSFER_IN)
                .status(TransactionStatus.POSTED)
                .direction(TransactionDirection.IN)
                .processedAt(now)
                .build();
    }

    public static Cashier transferOutLog(Long fromId,Long toId, BigDecimal balance, long money, BigDecimal after, LocalDateTime now){
        return Cashier.builder()
                .accountId(fromId)
                .counterpartyAccountId(toId)
                .amount(BigDecimal.valueOf(money))
                .balanceBefore(balance)
                .balanceAfter(after)
                .transactionType(TRANSFER_OUT)
                .status(TransactionStatus.POSTED)
                .direction(TransactionDirection.OUT)
                .processedAt(now)
                .build();
    }

    public void setTxId(Long id){
        this.transactionId=id;
    }

    public static Cashier withdrawLog(Long accountId, BigDecimal balance,BigDecimal after, long money, LocalDateTime now){
        return Cashier.builder()
                .accountId(accountId)
                .amount(BigDecimal.valueOf(money))
                .balanceBefore(balance)
                .balanceAfter(after)
                .transactionType(WITHDRAW)
                .status(TransactionStatus.POSTED)
                .direction(TransactionDirection.OUT)
                .processedAt(now)
                .build();
    }
}

