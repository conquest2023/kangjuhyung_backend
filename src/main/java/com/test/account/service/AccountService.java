package com.test.account.service;


import com.test.account.service.domain.Account;
import com.test.account.service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account saveAccount(){
        final int maxRetry = 10;
        for (int i = 0; i < maxRetry; i++) {
            Account account = Account.createAccount(LocalDateTime.now());
            try {
                return accountRepository.save(account);
            } catch (DataIntegrityViolationException e) {
                if (!isAccountNumberDuplicate(e))
                    throw e;
            }
        }
        throw new IllegalStateException("계좌번호 생성 재시도 초과");
    }

    public Account findAccount(Long accountId){

        return accountRepository.findByAccount(accountId);
    }

    public Account findAccountNumber(String accountNumber){

        return accountRepository.findByAccountNumber(accountNumber);
    }
    @Transactional
    public Long deleteAccount(String accountNumber){

        Account account = accountRepository.findByAccountNumber(accountNumber);
        account.close(LocalDateTime.now());
        accountRepository.deleteById(account.getAccountId());
        return account.getAccountId();
    }

    private boolean isAccountNumberDuplicate(DataIntegrityViolationException e) {

        Throwable cause = e.getMostSpecificCause();
        String msg = (cause != null ? cause.getMessage() : e.getMessage());
        return msg != null && msg.contains("uk_account_number");
    }
}
