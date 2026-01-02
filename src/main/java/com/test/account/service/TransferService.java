package com.test.account.service;

import com.test.account.exception.InvalidAmountException;
import com.test.account.exception.SelfTransferNotAllowedException;
import com.test.account.service.domain.Account;
import com.test.account.service.domain.Cashier;
import com.test.account.service.domain.enumtype.TransactionDirection;
import com.test.account.service.domain.enumtype.TransactionType;
import com.test.account.service.record.TransferResult;
import com.test.account.service.record.WithdrawResult;
import com.test.account.service.repository.AccountHistoryRepository;
import com.test.account.service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.test.account.service.domain.enumtype.TransactionType.WITHDRAW;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {


    private final AccountRepository accountRepository;

    private final AccountHistoryRepository accountHistoryRepository;

    @Transactional
    public Cashier deposit(String accountNumber, long money){

        Account account = accountRepository.findByAccountNumberWithLock(accountNumber);
//        Account account = accountRepository.findByAccountNumber(accountNumber);
        LocalDateTime now = LocalDateTime.now();
        BigDecimal before = account.getBalance();
        account.deposit(money,now);
        BigDecimal after = account.getBalance();
        accountRepository.save(account);
        Cashier log = Cashier.depositLog(account.getAccountId(), before,after, money, now);
        return accountHistoryRepository.saveTransactionLog(log,account);
    }
    @Transactional
    public WithdrawResult withdraw(String accountNumber, long money){
        Account account = accountRepository.findByAccountNumberWithLock(accountNumber);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        long withdrawnBefore = accountHistoryRepository.findWithdrawalLimit(account.getAccountId()
                ,startOfDay, WITHDRAW);
        account.validateWithdrawalLimit(withdrawnBefore,money);
        BigDecimal before = account.getBalance();
        account.withdraw(money,now);
        BigDecimal after = account.getBalance();
        accountRepository.save(account);
        Cashier log = Cashier.withdrawLog(account.getAccountId(),before,after, money, now);
        long withdrawnAfter = withdrawnBefore + money;
        Cashier cashier = accountHistoryRepository.saveTransactionLog(log, account);

        return new WithdrawResult(cashier,withdrawnAfter);
    }

    @Transactional
    public TransferResult transfer(String fromAccount, String toAccount, long amount) {
        if (amount <= 0) throw new InvalidAmountException();
        if (fromAccount.equals(toAccount)) throw new SelfTransferNotAllowedException();

        String first = fromAccount.compareTo(toAccount) < 0 ? fromAccount : toAccount;
        String second = fromAccount.compareTo(toAccount) < 0 ? toAccount : fromAccount;

        accountRepository.findByAccountNumberWithLock(first);
        accountRepository.findByAccountNumberWithLock(second);

        Account from = accountRepository.findByAccountNumber(fromAccount);
        Account to = accountRepository.findByAccountNumber(toAccount);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();

        long totalTransferBefore = accountHistoryRepository.findTransferLimit(from.getAccountId(), startOfDay, TransactionType.TRANSFER_OUT);
        from.validateTransferLimit(totalTransferBefore, amount);

        BigDecimal fromBefore = from.getBalance();
        BigDecimal toBefore = to.getBalance();

        BigDecimal totalWithdrawAmount = to.calculateCommission(BigDecimal.valueOf(amount));

        from.withdraw(totalWithdrawAmount.longValue(), now);
        to.deposit(amount, now);

        accountRepository.save(from);
        accountRepository.save(to);

        Cashier outLog = Cashier.transferOutLog(from.getAccountId(), to.getAccountId(), fromBefore, amount, from.getBalance(), now);
        Cashier inLog = Cashier.transferInLog(from.getAccountId(), to.getAccountId(), toBefore, amount, to.getBalance(), now);

        accountHistoryRepository.saveTransactionLog(inLog, to);
        Cashier cashier = accountHistoryRepository.saveTransactionLog(outLog, from);

        return new TransferResult(cashier, fromAccount, toAccount, totalTransferBefore + amount);
    }

}
