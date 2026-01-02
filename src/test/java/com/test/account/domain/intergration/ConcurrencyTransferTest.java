package com.test.account.domain.intergration;

import com.test.account.service.AccountService;
import com.test.account.service.TransferService;
import com.test.account.service.domain.Account;
import com.test.account.service.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ConcurrencyTransferTest {



    @Autowired
    private TransferService cashierService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;
    private String fromNo;
    private String toNo;

    @BeforeEach
    void setUp() {
        Account from = accountService.saveAccount();
        Account to = accountService.saveAccount();

        fromNo = from.getAccountNumber();
        toNo = to.getAccountNumber();

        cashierService.deposit(fromNo, 100_000L);
    }


    @Test
    @DisplayName("동시에 10건의 이체 요청 시 잔액이 정확해야 한다")
    void concurrentTransferTest() throws InterruptedException {
        int threadCount = 10;
        long amount = 1000L;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    cashierService.transfer(fromNo, toNo, amount);
                } catch (Exception e) {
                    System.err.println("실패 원인: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // 결과 확인
        Account finalFrom = accountRepository.findByAccountNumber(fromNo);
        // 100,000 - (1,000 * 10) = 90,000 (수수료 제외 로직일 경우)
        System.out.println("최종 잔액: " + finalFrom.getBalance());

        assertThat(finalFrom.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(89900));
    }
    @Test
    @DisplayName("A->B, B->A 교차 이체 시 데드락 없이 완료되어야 한다")
    void concurrentOppositeTransfers_shouldNotDeadlock() throws Exception {
        int threadCount = 2;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(threadCount);

        pool.submit(() -> {
            try {
                startSignal.await();
                cashierService.transfer(fromNo, toNo, 1000L);
            } catch (Exception ignored) {}
            finally { doneSignal.countDown();
            }
        });

        pool.submit(() -> {
            try {
                startSignal.await();
                cashierService.transfer(toNo, fromNo, 1000L);
            } catch (Exception ignored) {}
            finally { doneSignal.countDown(); }
        });

        startSignal.countDown();

        boolean finished = doneSignal.await(5, TimeUnit.SECONDS);
        assertThat(finished).as("데드락으로 인해 테스트가 종료되지 않았습니다").isTrue();

        pool.shutdown();
    }
}