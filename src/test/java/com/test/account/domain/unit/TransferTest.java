package com.test.account.domain.unit;

import com.test.account.exception.DailyTransferLimitExceededException;
import com.test.account.infrastructure.repository.test.AccountHistoryTestRepository;
import com.test.account.infrastructure.repository.test.AccountTestRepository;
import com.test.account.service.AccountService;
import com.test.account.service.TransferService;
import com.test.account.service.domain.Account;
import com.test.account.service.domain.Cashier;
import com.test.account.service.record.WithdrawResult;
import com.test.account.service.repository.AccountHistoryRepository;
import com.test.account.service.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
public class TransferTest {


    AccountRepository accountRepo;
    AccountHistoryRepository historyRepo;

    AccountService accountService;

    TransferService cashierService;

    @BeforeEach
    void setUp() {
        accountRepo = new AccountTestRepository();
        historyRepo = new AccountHistoryTestRepository();
        accountService = new AccountService(accountRepo);
        cashierService = new TransferService(accountRepo, historyRepo);
    }

    @Test
    @DisplayName("입금 테스트")
    void deposit() {
        Account account= accountService.saveAccount();
        System.out.println(account.getAccountNumber());
        System.out.println(account.getBalance());
        Cashier cashier = cashierService.deposit(account.getAccountNumber(), 10_000_000);
        Assertions.assertThat(cashier.getTransactionId()).isPositive();
        Account updated = accountService.findAccount(account.getAccountId());
        Assertions.assertThat(updated.getBalance()).isEqualByComparingTo("10000000");
    }

    @Test
    @DisplayName("출금 테스트")
    void withdraw() {
        Account account = accountService.saveAccount();
        long txId = cashierService.deposit(account.getAccountNumber(), 2000000).getTransactionId();
        Account updated = accountService.findAccount(account.getAccountId());
        String accountNumber = updated.getAccountNumber();
        cashierService.withdraw(accountNumber, 500000);
        WithdrawResult result = cashierService.withdraw(accountNumber, 500000);
        BigDecimal totalMoney = result.cashier().getBalanceAfter();
        Assertions.assertThat(totalMoney).isEqualByComparingTo("1000000");
    }


    @Test
    @DisplayName("출금 로그 테스트")
    void withdrawLog() {

        Account account = accountService.saveAccount();
        long txId = cashierService.deposit(account.getAccountNumber(), 2000000).getTransactionId();
        Account updated = accountService.findAccount(account.getAccountId());
        String accountNumber = updated.getAccountNumber();
        cashierService.withdraw(accountNumber,500000);
        WithdrawResult result = cashierService.withdraw(accountNumber, 500000);
        BigDecimal totalMoney = result.cashier().getBalanceAfter();
        Assertions.assertThat(updated.getBalance()).isEqualByComparingTo("1000000");
    }



    @Test
    @DisplayName("이체 통합 테스트 - 잔액 업데이트, 수수료 차감, 한도 초과 검증")
    void transferTest() {
        Account fromAccount = accountService.saveAccount();
        Account toAccount = accountService.saveAccount();

        String fromNo = fromAccount.getAccountNumber();
        String toNo = toAccount.getAccountNumber();

        cashierService.deposit(fromNo, 5000000);
        cashierService.deposit(toNo, 10000);

        cashierService.transfer(fromNo, toNo, 1000000);
        cashierService.transfer(fromNo, toNo, 1000000);

        Account updatedFrom = accountService.findAccount(fromAccount.getAccountId());
        Account updatedTo = accountService.findAccount(toAccount.getAccountId());

        Assertions.assertThat(updatedFrom.getBalance()).isEqualByComparingTo("2980000");
        Assertions.assertThat(updatedTo.getBalance()).isEqualByComparingTo("2010000");

        Assertions.assertThatThrownBy(() ->
                cashierService.transfer(fromNo, toNo, 2000000)
        ).isInstanceOf(DailyTransferLimitExceededException.class);
    }
    @Test
    @DisplayName("출금 경계값 - 잔액이 정확히 0원이 되는 경우")
    void withdrawToZero() {
        Account account = accountService.saveAccount();
        cashierService.deposit(account.getAccountNumber(), 10000);

        cashierService.withdraw(account.getAccountNumber(), 10000);

        Account updated = accountService.findAccount(account.getAccountId());
        Assertions.assertThat(updated.getBalance()).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("출금 실패 - 잔액 부족 시 예외 발생")
    void withdrawFailInsufficientBalance() {
        Account account = accountService.saveAccount();
        cashierService.deposit(account.getAccountNumber(), 5000);

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            cashierService.withdraw(account.getAccountNumber(), 5001);
        });
    }

    @Test
    @DisplayName("이체 한도 테스트 - 1일 총 한도 초과 시 실패")
    void transferLimitExceeded() {
        Account from = accountService.saveAccount();
        Account to = accountService.saveAccount();

        cashierService.deposit(from.getAccountNumber(), 4000000);

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            cashierService.transfer(from.getAccountNumber(), to.getAccountNumber(), 3000001);
        });
    }

    @Test
    @DisplayName("이체 정밀도 테스트 - 여러 번 이체 후 최종 잔액 검증")
    void transferPrecision() {
        Account from = accountService.saveAccount();
        Account to = accountService.saveAccount();

        cashierService.deposit(from.getAccountNumber(), 100000);

        long amount = 12345;
        cashierService.transfer(from.getAccountNumber(), to.getAccountNumber(), amount);
        cashierService.transfer(from.getAccountNumber(), to.getAccountNumber(), amount);
        cashierService.transfer(from.getAccountNumber(), to.getAccountNumber(), amount);

        Account fromResult = accountService.findAccount(from.getAccountId());
        Account toResult = accountService.findAccount(to.getAccountId());

        Assertions.assertThat(fromResult.getBalance()).isEqualByComparingTo("62596");
        Assertions.assertThat(toResult.getBalance()).isEqualByComparingTo("37035");
    }
}
