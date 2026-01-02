package com.test.account.service.repository;

import com.test.account.controller.dto.account.AccountTimeHistoryCondition;
import com.test.account.controller.dto.time.TimeRange;
import com.test.account.service.domain.AccountHistory;
import com.test.account.service.domain.enumtype.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountHistoryQueryRepository {


    Page<AccountHistory> search(
            int page,
            int size,
            Long accountId,
            TransactionType type,
            TimeRange range
    );



    Page<AccountHistory> findTransactionHistory(int page, int size, TransactionType type, Long accountId);
}
