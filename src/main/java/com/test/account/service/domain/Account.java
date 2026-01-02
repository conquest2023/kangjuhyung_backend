package com.test.account.service.domain;

import com.test.account.exception.DailyTransferLimitExceededException;
import com.test.account.exception.DailyWithdrawLimitExceededException;
import com.test.account.exception.InsufficientBalanceException;
import com.test.account.service.domain.enumtype.AccountStatus;
import com.test.account.service.domain.enumtype.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Random;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.test.account.service.domain.enumtype.AccountStatus.*;
import static com.test.account.service.domain.enumtype.AccountType.CHECKING;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private Long accountId;

    private String accountNumber;

    private BigDecimal balance;

    private AccountStatus status;

    private AccountType accountType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private LocalDateTime lastTransactionAt;


    public static Account createAccount(LocalDateTime now){
        return Account.builder()
                .balance(BigDecimal.ZERO)
                .status(ACTIVE)
                .accountType(CHECKING)
                .accountNumber(generateAccountNumber())
                .createdAt(now)
                .build();
    }

    public void close(LocalDateTime now){
        this.status=CLOSED;
        this.deletedAt=now;
    }
    public void deposit(long money,LocalDateTime now){
        this.balance=BigDecimal.valueOf(money).add(balance);
        this.lastTransactionAt=now;
    }

    public  void transfer(long amount,LocalDateTime now){
        this.balance=balance.subtract(BigDecimal.valueOf(amount));
        this.lastTransactionAt=now;
    }
    public BigDecimal calculateCommission(BigDecimal balance) {
        BigDecimal rate = new BigDecimal("0.01");
        BigDecimal commissionAmount = balance.multiply(rate);
        return balance.add(commissionAmount);
    }
    public void withdraw(long money, LocalDateTime now) {
        if (BigDecimal.valueOf(money).compareTo(balance) > 0) {
            throw new InsufficientBalanceException(
                    "잔액이 부족합니다. 최대 " + balance.intValue() + "원까지 송금이 가능합니다"
            );
        }
        this.balance = this.balance.subtract(BigDecimal.valueOf(money));
        this.lastTransactionAt = now;
    }

    public void validateWithdrawalLimit(long totalWithdraw, long money){
        long limit = 1_000_000L;
        if (totalWithdraw + money > limit) {
            throw new DailyWithdrawLimitExceededException(
                    "오늘 하루 출금 금액을 초과하셨습니다. 현재 " + (limit - totalWithdraw) + "원까지 더 출금 가능합니다"
            );
        }
    }
    public void validateTransferLimit(long totalTransfer, long money) {
        long limit = 3_000_000L;
        if (totalTransfer + money > limit) {
            throw new DailyTransferLimitExceededException(
                    "오늘 하루 이체 금액을 초과하셨습니다. 현재 " + (limit - totalTransfer) + "원까지 더 이체 가능합니다."
            );
        }
    }

    public boolean isCheckAccountNumber(String newAccountNumber,String existAccountNumber){
        return newAccountNumber.equals(existAccountNumber);
    }
    public static String generateAccountNumber() {
        Random random = new Random();
        return String.format(
                "%04d-%04d-%04d-%04d",
                1234, // 은행 코드
                random.nextInt(10000),
                random.nextInt(10000),
                random.nextInt(10000)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(accountId, account.accountId) && Objects.equals(accountNumber, account.accountNumber) && Objects.equals(balance, account.balance) && status == account.status && accountType == account.accountType && Objects.equals(createdAt, account.createdAt) && Objects.equals(updatedAt, account.updatedAt) && Objects.equals(deletedAt, account.deletedAt) && Objects.equals(lastTransactionAt, account.lastTransactionAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountNumber, balance, status, accountType, createdAt, updatedAt, deletedAt, lastTransactionAt);
    }
}
