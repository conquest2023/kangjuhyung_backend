package com.test.account.service.repository;

import com.test.account.service.domain.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository {


    Account findByAccountNumber(String accountNumber);

    Account findByAccountNumberWithLock(String accountNumber);
    Account save(Account account);

    Account findByAccount(Long id);

    Long deleteById(Long accountId);
}
