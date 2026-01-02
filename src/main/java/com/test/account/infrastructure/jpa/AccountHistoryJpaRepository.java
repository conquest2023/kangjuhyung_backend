package com.test.account.infrastructure.jpa;

import com.test.account.infrastructure.entity.AccountTransactionEntity;
import com.test.account.service.domain.enumtype.TransactionDirection;
import com.test.account.service.domain.enumtype.TransactionStatus;
import com.test.account.service.domain.enumtype.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AccountHistoryJpaRepository extends JpaRepository<AccountTransactionEntity,Long> {



        @Query("""
        select coalesce(sum(a.amount), 0)
        from AccountTransactionEntity a
        where a.account.accountId = :accountId
          and a.type = :type
          and a.createdAt >= :createdAt
    """)
        BigDecimal sumWithdrawalAmount(
                @Param("accountId") Long accountId,
                @Param("createdAt") LocalDateTime createdAt,
                @Param("type") TransactionType type
        );

        @Query("""
        select coalesce(sum(a.amount), 0)
        from AccountTransactionEntity a
        where a.account.accountId = :accountId
          and a.type= :type
          and a.createdAt >= :createdAt
    """)
        BigDecimal sumTransferAmount(
                @Param("accountId") Long accountId,
                @Param("createdAt") LocalDateTime createdAt,
                @Param("type")TransactionType type
        );

}
