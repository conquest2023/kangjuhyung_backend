package com.test.account.infrastructure.jpa;


import com.test.account.infrastructure.entity.AccountEntity;
import com.test.account.service.domain.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity,Long>{



    @Query("select a from AccountEntity a where a.accountNumber=:accountNumber")
    Optional<AccountEntity> findByAccountNumber(@Param("accountNumber") String accountNumber);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from AccountEntity a where a.accountNumber = :accountNumber")
    Optional<AccountEntity> findByAccountNumberWithLock(@Param("accountNumber") String accountNumber);

    @Query("""
    select count(a) > 0
    from AccountEntity a
    where a.accountNumber = :accountNumber""")
    boolean existsByAccountNumber(@Param("accountNumber") String accountNumber);
}
