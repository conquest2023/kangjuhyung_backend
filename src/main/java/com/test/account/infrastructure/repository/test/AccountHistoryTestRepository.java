package com.test.account.infrastructure.repository.test;

import com.test.account.service.domain.Account;
import com.test.account.service.domain.Cashier;
import com.test.account.service.domain.enumtype.TransactionDirection;
import com.test.account.service.domain.enumtype.TransactionType;
import com.test.account.service.repository.AccountHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
public class AccountHistoryTestRepository implements AccountHistoryRepository {

    private final Map<Long, List<Cashier>> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1L);



    @Override
    public Cashier saveTransactionLog(Cashier txLog, Account account) {
        Long newId = sequence.getAndIncrement();

        txLog.setTxId(newId);

        Long accountId = account.getAccountId();
        store.computeIfAbsent(accountId, k -> new ArrayList<>())
                .add(txLog);

        return txLog;
    }



    @Override
    public Long findWithdrawalLimit(Long accountId, LocalDateTime now, TransactionType type) {
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();

        long sum = store.getOrDefault(accountId, List.of()).stream()
                .filter(log -> log.getTransactionType() == type)
                .filter(log -> !log.getProcessedAt().isBefore(startOfDay))
                .map(Cashier::getAmount)
                .mapToLong(BigDecimal::longValue)
                .sum();
        return sum;
    }

    @Override
    public long findTransferLimit(Long accountId, LocalDateTime now, TransactionType type) {
        List<Cashier> history = store.getOrDefault(accountId, Collections.emptyList());

        BigDecimal totalAmount = history.stream()
                .filter(c -> c.getTransactionType() == type)
                .filter(c -> c.getProcessedAt().isAfter(now) || c.getProcessedAt().isEqual(now))
                .map(Cashier::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalAmount.longValue();
    }
}
