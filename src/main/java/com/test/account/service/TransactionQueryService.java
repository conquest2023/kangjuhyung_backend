package com.test.account.service;

import com.test.account.controller.dto.account.AccountTimeHistoryCondition;
import com.test.account.controller.dto.time.TimeRange;
import com.test.account.service.domain.Account;
import com.test.account.service.domain.AccountHistory;
import com.test.account.service.domain.enumtype.TransactionType;
import com.test.account.service.repository.AccountHistoryQueryRepository;
import com.test.account.service.repository.AccountRepository;
import com.test.account.util.TimeRangeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionQueryService {


    private final AccountRepository accountRepository;

    private final AccountHistoryQueryRepository accountHistoryQueryRepository;

    public Page<AccountHistory> findTimeTransactionHistory(int page , int size, String accountNumber,
                                                           AccountTimeHistoryCondition
                                                            cond){

        Account account = accountRepository.findByAccountNumber(accountNumber);
        LocalDateTime now = LocalDateTime.now();
        TransactionType type = TransactionType.fromDescription(cond.getType());
        TimeRange range = TimeRangeResolver.resolve(cond.getPreset(), now);
        return accountHistoryQueryRepository.search(page,size,account.getAccountId(),type,range);
    }


    public Page<AccountHistory> findTransactionHistory(int page, int size, TransactionType type, String accountNumber){

        Account account = accountRepository.findByAccountNumber(accountNumber);

        return accountHistoryQueryRepository.findTransactionHistory(page,size,type,account.getAccountId());
    }



}
