package com.test.account.domain.intergration;


import com.test.account.exception.DailyTransferLimitExceededException;
import com.test.account.exception.InsufficientBalanceException;
import com.test.account.exception.SelfTransferNotAllowedException;
import com.test.account.service.AccountService;
import com.test.account.service.TransferService;
import com.test.account.service.TransactionQueryService;
import com.test.account.service.domain.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TransferIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransferService cashierService;

    @Autowired
    private TransactionQueryService queryService;
    @Test
    @DisplayName("실제 DB 연동 이체 테스트 (롤백 보장)")
     void realDbTransferTest() {
        Account from = accountService.saveAccount();
        Account to = accountService.saveAccount();

        cashierService.deposit(from.getAccountNumber(), 100000);
        cashierService.deposit(to.getAccountNumber(), 50000);

        long transferAmount = 10000;
        cashierService.transfer(from.getAccountNumber(), to.getAccountNumber(), transferAmount);

        Account updatedFrom = accountService.findAccount(from.getAccountId());
        Account updatedTo = accountService.findAccount(to.getAccountId());

        Assertions.assertThat(updatedFrom.getBalance()).isEqualByComparingTo("89900");

        Assertions.assertThat(updatedTo.getBalance()).isEqualByComparingTo("60000");
    }

    @Test
    @DisplayName("이체 한도 빡세게 검증 - 여러 번 나눠서 보낼 때 누적 한도 초과 확인")
    void transferLimitCumulativeTest() {
        Account from = accountService.saveAccount();
        Account to = accountService.saveAccount();
        cashierService.deposit(from.getAccountNumber(), 5000000);

        cashierService.transfer(from.getAccountNumber(), to.getAccountNumber(), 1000000);
        cashierService.transfer(from.getAccountNumber(), to.getAccountNumber(), 1000000);

        Assertions.assertThatThrownBy(() ->
                cashierService.transfer(from.getAccountNumber(), to.getAccountNumber(), 1000001)
        ).isInstanceOf(DailyTransferLimitExceededException.class);
    }
    @Test
    @DisplayName("잔액 부족 검증 - 수수료를 포함하면 잔액이 부족해지는 경우")
    void withdrawFailWithCommission() {
        Account account = accountService.saveAccount();
        cashierService.deposit(account.getAccountNumber(), 10050);

        Assertions.assertThatThrownBy(() ->
                cashierService.transfer(account.getAccountNumber(), "1234-7145-4549-7511", 10000)
        ).isInstanceOf(InsufficientBalanceException.class);
    }

    @Test
    @DisplayName("자기 자신에게 이체 시도 차단 테스트")
    void selfTransferTest() {
        Account account = accountService.saveAccount();
        cashierService.deposit(account.getAccountNumber(), 100000);

        String accNum = account.getAccountNumber();

        Assertions.assertThatThrownBy(() ->
                cashierService.transfer(accNum, accNum, 10000)
        ).isExactlyInstanceOf(SelfTransferNotAllowedException.class);
    }
    @Test
    @Rollback(false)
    @DisplayName("실제 DB 연동 이체 테스트 (롤백 취소)")
    void realDbCommitTransferTest() {
        Account from = accountService.saveAccount();
        Account to = accountService.saveAccount();

        cashierService.deposit(from.getAccountNumber(), 100000);
        cashierService.deposit(to.getAccountNumber(), 50000);

        long transferAmount = 10000;
        cashierService.transfer(from.getAccountNumber(), to.getAccountNumber(), transferAmount);

        Account updatedFrom = accountService.findAccount(from.getAccountId());
        Account updatedTo = accountService.findAccount(to.getAccountId());

        Assertions.assertThat(updatedFrom.getBalance()).isEqualByComparingTo("89900");

        Assertions.assertThat(updatedTo.getBalance()).isEqualByComparingTo("60000");
    }
}
