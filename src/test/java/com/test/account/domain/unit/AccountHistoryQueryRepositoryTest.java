package com.test.account.domain.unit;

import com.test.account.controller.dto.time.TimeRange;
import com.test.account.infrastructure.repository.test.AccountHistoryQueryTestRepository;
import com.test.account.service.domain.AccountHistory;
import com.test.account.service.domain.Cashier;
import com.test.account.service.domain.enumtype.TransactionType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class AccountHistoryQueryRepositoryTest {

    private AccountHistoryQueryTestRepository repository;



    @BeforeEach
    void setUp() {
        repository = new AccountHistoryQueryTestRepository();
    }

    @Test
    @DisplayName("메모리 DB 조회 테스트 - 타입 필터링 및 최신순 정렬 확인")
    void searchTest() {
        Long accountId = 1L;
        LocalDateTime now = LocalDateTime.now();

        repository.save(accountId, createLog(TransactionType.DEPOSIT, now.minusDays(2), 10000));
        repository.save(accountId, createLog(TransactionType.TRANSFER_OUT, now.minusDays(1), 5000));
        repository.save(accountId, createLog(TransactionType.TRANSFER_OUT, now, 2000));

        TimeRange range = new TimeRange(now.minusDays(3), now.plusDays(1));
        Page<AccountHistory> result = repository.search(0, 10, accountId, TransactionType.TRANSFER_OUT, range);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getContent().get(0).getAmount()).isEqualByComparingTo("2000");
        Assertions.assertThat(result.getContent().get(1).getAmount()).isEqualByComparingTo("5000");
    }

    @Test
    @DisplayName("페이징 테스트 - 데이터가 많을 때 특정 페이지의 데이터만 가져오는지 확인")
    void pagingTest() {
        Long accountId = 1L;
        LocalDateTime now = LocalDateTime.now();

        for (int i = 1; i <= 15; i++) {
            repository.save(accountId, createLog(TransactionType.TRANSFER_OUT, now.minusMinutes(i), i * 1000));
        }

        TimeRange range = new TimeRange(now.minusDays(1), now.plusDays(1));
        Page<AccountHistory> firstPage = repository.search(0, 10, accountId, TransactionType.TRANSFER_OUT, range);

        Page<AccountHistory> secondPage = repository.search(1, 10, accountId, TransactionType.TRANSFER_OUT, range);

        Assertions.assertThat(firstPage.getContent()).hasSize(10);
        Assertions.assertThat(secondPage.getContent()).hasSize(5);
        Assertions.assertThat(firstPage.getTotalElements()).isEqualTo(15);
    }
    @Test
    @DisplayName("기간 조회 테스트 - 시작/종료 시간 정각 데이터는 제외됨 (Exclusive)")
    void timeRangeExclusiveTest() {
        Long accountId = 1L;
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 11, 0);

        repository.save(accountId, createLog(TransactionType.DEPOSIT, start, 1000));
        repository.save(accountId, createLog(TransactionType.DEPOSIT, start.plusMinutes(30), 2000));
        repository.save(accountId, createLog(TransactionType.DEPOSIT, end, 3000));

        TimeRange range = new TimeRange(start, end);
        Page<AccountHistory> result = repository.search(0, 10, accountId, TransactionType.DEPOSIT, range);

        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent().get(0).getAmount()).isEqualByComparingTo("2000");
    }
    @Test
    @DisplayName("필터 미지정 테스트 - TransactionType이 null일 경우 모든 타입 조회")
    void allTypeSearchTest() {
        Long accountId = 1L;
        LocalDateTime now = LocalDateTime.now();

        repository.save(accountId, createLog(TransactionType.DEPOSIT, now, 1000));
        repository.save(accountId, createLog(TransactionType.TRANSFER_OUT, now.plusMinutes(1), 2000));
        repository.save(accountId, createLog(TransactionType.WITHDRAW, now.plusMinutes(2), 3000));

        TimeRange range = new TimeRange(now.minusDays(1), now.plusDays(1));
        Page<AccountHistory> result = repository.search(0, 10, accountId, null, range);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(3);
    }
    private Cashier createLog(TransactionType type, LocalDateTime time, long amount) {
        return Cashier.builder()
                .transactionType(type)
                .amount(BigDecimal.valueOf(amount))
                .processedAt(time)
                .build();
    }
}