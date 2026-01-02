package com.test.account.service.repository;

import com.test.account.service.domain.Account;
import com.test.account.service.domain.Cashier;
import com.test.account.service.domain.enumtype.TransactionDirection;
import com.test.account.service.domain.enumtype.TransactionStatus;
import com.test.account.service.domain.enumtype.TransactionType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface AccountHistoryRepository {

    Cashier saveTransactionLog(Cashier txLog, Account account);

    Long findWithdrawalLimit(Long accountId, LocalDateTime now, TransactionType type);

    long findTransferLimit(Long accountId, LocalDateTime now, TransactionType type);
}
