package com.test.account.infrastructure.repository;

import com.test.account.controller.dto.account.AccountTimeHistoryCondition;
import com.test.account.controller.dto.time.TimeRange;
import com.test.account.infrastructure.entity.AccountTransactionEntity;
import com.test.account.infrastructure.jpa.AccountHistoryQueryJpaRepository;
import com.test.account.mapper.AccountHistoryMapper;
import com.test.account.service.domain.AccountHistory;
import com.test.account.service.domain.enumtype.TransactionType;
import com.test.account.service.repository.AccountHistoryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountHistoryQueryAdapterRepository implements AccountHistoryQueryRepository {

    private final AccountHistoryQueryJpaRepository accountHistoryQueryJpaRepository;
    @Override
    public Page<AccountHistory> search(int page, int size, Long accountId, TransactionType type, TimeRange range) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<AccountTransactionEntity> rows;
        if (type == null) {
            rows = accountHistoryQueryJpaRepository.findByTimeRangeAccountHistory(
                    accountId, range.from(), range.to(), pageable
            );
        } else {
            rows = accountHistoryQueryJpaRepository.findByRangeAccountHistory(
                    accountId, type, range.from(), range.to(), pageable
            );
        }
        return rows.map(AccountHistoryMapper::toDomain);
    }

    @Override
    public Page<AccountHistory> findTransactionHistory(int page, int size, TransactionType type, Long accountId) {

        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<AccountTransactionEntity> rows =
                accountHistoryQueryJpaRepository.findByAccountHistory(
                        accountId,
                        type,
                        pageable);
        return rows.map(AccountHistoryMapper::toDomain);

    }
}
