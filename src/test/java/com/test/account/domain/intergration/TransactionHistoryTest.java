package com.test.account.domain.intergration;

import com.test.account.controller.dto.account.AccountTimeHistoryCondition;
import com.test.account.controller.dto.time.TimePreset;
import com.test.account.service.AccountService;
import com.test.account.service.TransferService;
import com.test.account.service.TransactionQueryService;
import com.test.account.service.domain.Account;
import com.test.account.service.domain.AccountHistory;
import com.test.account.service.domain.enumtype.TransactionType;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
@Transactional
public class TransactionHistoryTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionQueryService transactionQueryService;

    @Autowired
    private TransferService cashierService;

    @Test
    @DisplayName("기간 및 타입 조건 검색 통합 테스트")
    void findTimeTransactionHistoryIntegrationTest() {
        Account from = accountService.saveAccount();
        Account to = accountService.saveAccount();
        String accNum = from.getAccountNumber();
        String toNum = to.getAccountNumber();
        cashierService.deposit(accNum, 10000);

        cashierService.transfer(accNum, toNum, 5000);

        AccountTimeHistoryCondition cond = new AccountTimeHistoryCondition();
        cond.setPreset(TimePreset.LAST_1_HOUR);
        cond.setType("송금");

        Page<AccountHistory> result = transactionQueryService.findTimeTransactionHistory(0, 10, accNum, cond);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getContent().get(0).getType())
                .isEqualTo(TransactionType.TRANSFER_OUT);
        Assertions.assertThat(result.getContent().get(0).getAmount().longValue())
                .isEqualTo(5000);
    }

    @Test
    @DisplayName("단순 타입 필터링 통합 테스트")
    void findTransactionHistoryIntegrationTest() {

        Account account = accountService.saveAccount();
        String accNum = account.getAccountNumber();

        cashierService.deposit(accNum, 20000);
        cashierService.deposit(accNum, 40000);
        cashierService.withdraw(accNum, 5000);

        Page<AccountHistory> result = transactionQueryService.findTransactionHistory(0, 10,TransactionType.DEPOSIT, accNum);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getContent().get(0).getType())
                .isEqualTo(TransactionType.DEPOSIT);
    }
}
