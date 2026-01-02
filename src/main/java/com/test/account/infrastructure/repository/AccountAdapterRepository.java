package com.test.account.infrastructure.repository;

import com.test.account.exception.AccountNotFoundException;
import com.test.account.infrastructure.entity.AccountEntity;
import com.test.account.infrastructure.jpa.AccountJpaRepository;
import com.test.account.mapper.AccountMapper;
import com.test.account.service.domain.Account;
import com.test.account.service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountAdapterRepository implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;
    @Override
    public Account findByAccountNumber(String accountNumber) {
        AccountEntity accountEntity = accountJpaRepository.findByAccountNumber(accountNumber)
              .orElseThrow(AccountNotFoundException::new);
        return AccountMapper.toDomain(accountEntity);
    }

    @Override
    public Account findByAccountNumberWithLock(String accountNumber) {
        AccountEntity accountEntity = accountJpaRepository.findByAccountNumberWithLock(accountNumber)
                .orElseThrow(AccountNotFoundException::new);
        return AccountMapper.toDomain(accountEntity);
    }


    @Override
    public Account save(Account account) {
        AccountEntity entity = AccountMapper.toEntity(account);
        AccountEntity saveEntity = accountJpaRepository.save(entity);

        return AccountMapper.toDomain(saveEntity);
    }

    @Override
    public Account findByAccount(Long id) {
        Optional<AccountEntity> accountEntity = accountJpaRepository.findById(id);
        if( accountEntity.isPresent()){
            return  AccountMapper.toDomain(accountEntity.get());
        } else {
            throw new AccountNotFoundException();
        }
    }
    @Override
    public Long deleteById(Long accountId) {
        accountJpaRepository.deleteById(accountId);

        return accountId;
    }
}
