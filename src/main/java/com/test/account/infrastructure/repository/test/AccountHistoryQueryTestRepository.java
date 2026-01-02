package com.test.account.infrastructure.repository.test;

import com.test.account.controller.dto.time.TimeRange;
import com.test.account.mapper.AccountHistoryMapper;
import com.test.account.mapper.AccountMapper;
import com.test.account.service.domain.AccountHistory;
import com.test.account.service.domain.Cashier;
import com.test.account.service.domain.enumtype.TransactionType;
import com.test.account.service.repository.AccountHistoryQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class AccountHistoryQueryTestRepository implements AccountHistoryQueryRepository {

    private final Map<Long, List<Cashier>> store = new ConcurrentHashMap<>();

    @Override
    public Page<AccountHistory> search(int page, int size, Long accountId, TransactionType type, TimeRange range) {
        List<Cashier> histories = store.getOrDefault(accountId, new ArrayList<>());

        List<AccountHistory> filtered = histories.stream()
                .filter(c -> type == null || c.getTransactionType() == type)
                .filter(c -> range == null || (c.getProcessedAt()).isAfter(range.from()) && c.getProcessedAt().isBefore(range.to()))
                .sorted(Comparator.comparing(Cashier::getProcessedAt).reversed())
                .map(AccountHistoryMapper::toHistoryDomain)
                .collect(Collectors.toList());

        return createPage(filtered, page, size);
    }

    @Override
    public Page<AccountHistory> findTransactionHistory(int page, int size, TransactionType type, Long accountId) {
        List<Cashier> histories = store.getOrDefault(accountId, new ArrayList<>());

        List<AccountHistory> filtered = histories.stream()
                .filter(c -> type == null || c.getTransactionType() == type)
                .sorted(Comparator.comparing(Cashier::getProcessedAt).reversed())
                .map(AccountHistoryMapper::toHistoryDomain)
                .collect(Collectors.toList());

        return createPage(filtered, page, size);
    }
    public void save(Long accountId, Cashier cashier) {
        store.computeIfAbsent(accountId, k -> new ArrayList<>()).add(cashier);
    }
    private Page<AccountHistory> createPage(List<AccountHistory> list, int page, int size) {
        int start = page * size;
        int end = Math.min((start + size), list.size());

        if (start > list.size()) {
            return new PageImpl<>(new ArrayList<>(), PageRequest.of(page, size), list.size());
        }

        return new PageImpl<>(list.subList(start, end), PageRequest.of(page, size), list.size());
    }

}
