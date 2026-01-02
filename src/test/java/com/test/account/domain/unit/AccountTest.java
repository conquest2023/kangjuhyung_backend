package com.test.account.domain.unit;

import com.test.account.exception.AccountNotFoundException;
import com.test.account.infrastructure.repository.test.AccountHistoryTestRepository;
import com.test.account.infrastructure.repository.test.AccountTestRepository;
import com.test.account.service.AccountService;
import com.test.account.service.TransferService;
import com.test.account.service.domain.Account;
import com.test.account.service.domain.enumtype.AccountStatus;
import com.test.account.service.repository.AccountHistoryRepository;
import com.test.account.service.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

//@SpringBootTest
@Transactional
public class AccountTest {

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
    @DisplayName("계좌번호 생성 로직")
    public void createAccountNumberTest() {
        String accountNumber = Account.generateAccountNumber();
        String prefix = accountNumber.substring(0, 4);
        int accountLength = accountNumber.length();

        Assertions.assertThat(accountLength).isEqualTo(19);

        Assertions.assertThat(prefix).isEqualTo("1234");
        Assertions.assertThat(accountNumber).matches("^\\d{4}-\\d{4}-\\d{4}-\\d{4}$");

        System.out.println("Result: " + accountNumber);
    }


    @Test
    @DisplayName("계좌생성 로직")
    public void createAccountTest() {

        Account account= accountService.saveAccount();
        Account account2= accountService.saveAccount();
        Assertions.assertThat(account.getAccountId()).isEqualTo(1L);
        Assertions.assertThat(account2.getAccountId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("계좌찾기 로직")
    public void findAccountTest() {
        AccountService accountService = new AccountService(new AccountTestRepository());
        Account account = accountService.saveAccount();
        //오류
        assertThatThrownBy(() -> accountService.findAccount(999L))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage("계좌가 존재하지 않습니다.");

        //찾기
        Assertions.assertThat(account.getAccountId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("계좌삭제 로직")
    public void deleteAccountTest() {
        Account account= accountService.saveAccount();
        accountService.deleteAccount(account.getAccountNumber());
        Assertions.assertThat(account.getStatus()).isEqualTo(AccountStatus.CLOSED);

    }


}
