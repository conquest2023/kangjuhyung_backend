package com.test.account.infrastructure.jpa;

import com.test.account.infrastructure.entity.AccountTransactionEntity;
import com.test.account.service.domain.enumtype.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AccountHistoryQueryJpaRepository extends JpaRepository<AccountTransactionEntity,Long> {



    @Query("""
    select a
    from AccountTransactionEntity a
    where a.account.accountId = :accountId
      and a.type=:type
      and a.createdAt >= :from
      and a.createdAt < :to
""")
    Page<AccountTransactionEntity> findByRangeAccountHistory(
            @Param("accountId") Long accountId,
            @Param("type") TransactionType type,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );

    @Query("""
    select a
    from AccountTransactionEntity a
    where a.account.accountId = :accountId
      and a.createdAt >= :from
      and a.createdAt < :to
    """)
    Page<AccountTransactionEntity> findByTimeRangeAccountHistory(
            @Param("accountId") Long accountId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );

    @Query("""
    select a
    from AccountTransactionEntity a
    where a.account.accountId=:accountId
     and  a.type=:type
     """)
    Page<AccountTransactionEntity> findByAccountHistory(
            @Param("accountId") Long accountId,
            @Param("type") TransactionType type,
            Pageable pageable
    );

}
