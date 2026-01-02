package com.test.account.infrastructure.repository;

import com.test.account.infrastructure.entity.AccountEntity;
import com.test.account.infrastructure.entity.AccountTransactionEntity;
import com.test.account.infrastructure.jpa.AccountHistoryJpaRepository;
import com.test.account.infrastructure.jpa.AccountJpaRepository;
import com.test.account.mapper.AccountMapper;
import com.test.account.mapper.AccountTransactionMapper;
import com.test.account.service.domain.Account;
import com.test.account.service.domain.Cashier;
import com.test.account.service.domain.enumtype.TransactionDirection;
import com.test.account.service.domain.enumtype.TransactionStatus;
import com.test.account.service.domain.enumtype.TransactionType;
import com.test.account.service.repository.AccountHistoryRepository;
import com.test.account.service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.lang.ref.PhantomReference;
import java.time.LocalDateTime;
@Slf4j
@Repository
@RequiredArgsConstructor
public class AccountHistoryAdapterRepository implements AccountHistoryRepository {

    private final AccountHistoryJpaRepository repository;

    private final AccountJpaRepository accountJpaRepository;

    @Override
    public Cashier saveTransactionLog(Cashier txLog,Account account) {
        AccountEntity accountRef = accountJpaRepository.getReferenceById(account.getAccountId());
        AccountTransactionEntity entity = AccountTransactionMapper.toEntity(txLog, accountRef);
        AccountTransactionEntity save = repository.save(entity);

        return AccountTransactionMapper.toDomain(save);
    }


    @Override
    public Long findWithdrawalLimit(Long accountId, LocalDateTime now, TransactionType type) {
        return repository.sumWithdrawalAmount(accountId,now,type).longValue();
    }

    @Override
    public long findTransferLimit(Long accountId, LocalDateTime now, TransactionType type) {
        return repository.sumTransferAmount(accountId,now,type).longValue();
    }
}
