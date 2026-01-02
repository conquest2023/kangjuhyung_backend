package com.test.account.infrastructure.repository.test;

import com.test.account.exception.AccountNotFoundException;
import com.test.account.service.domain.Account;
import com.test.account.service.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class AccountTestRepository implements AccountRepository {

    private final Map<Long,Account> store=new ConcurrentHashMap<>();

    private final AtomicLong sequence = new AtomicLong(1L);


    @Override
    public Account findByAccountNumber(String accountNumber) {
        return store.values().stream()
                .filter(account -> accountNumber.equals(account.getAccountNumber()))
                .findFirst()
                .get();
    }
    @Override
    public Account findByAccountNumberWithLock(String accountNumber) {
        return store.values().stream()
                .filter(account -> accountNumber.equals(account.getAccountNumber()))
                .findFirst()
                .get();
    }
    @Override
    public Account save(Account account) {
        Long id = sequence.getAndIncrement();
        Account saved = Account.builder()
                .accountId(id)
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .status(account.getStatus())
                .accountType(account.getAccountType())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .deletedAt(account.getDeletedAt())
                .lastTransactionAt(account.getLastTransactionAt())
                .build();

        store.put(id, saved);
        return saved;
    }
    @Override
    public Account findByAccount(Long accountId) {
        Account account = store.get(accountId);
        if (account==null){
            throw new AccountNotFoundException();
        }
        return account;
    }

    @Override
    public Long deleteById(Long accountId) {
        Account account = store.get(accountId);
        if (account==null){
            throw new AccountNotFoundException();
        }
        return account.getAccountId();
    }
}
