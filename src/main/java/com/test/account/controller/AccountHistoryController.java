package com.test.account.controller;


import com.test.account.controller.dto.account.AccountHistoryResponse;
import com.test.account.controller.dto.account.AccountTimeHistoryCondition;
import com.test.account.service.TransactionQueryService;
import com.test.account.service.domain.AccountHistory;
import com.test.account.service.domain.enumtype.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountHistoryController {


    private final TransactionQueryService accountService;

    @GetMapping("/accounts/{accountNumber}/histories")
    public Page<AccountHistoryResponse> search(
            @PathVariable String accountNumber,
            @ModelAttribute AccountTimeHistoryCondition cond,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<AccountHistory> result = accountService.findTimeTransactionHistory(page,size,accountNumber, cond);
        return result.map(AccountHistoryResponse::from);
    }

    @GetMapping("/{accountNumber}/histories")
    public Page<AccountHistoryResponse> getTransactionHistory(
            @PathVariable String accountNumber,
            @RequestParam(required = false) String type,
            @RequestParam int page,
            @RequestParam int size) {

        TransactionType transactionType = TransactionType.fromDescription(type);
        Page<AccountHistory> transactionHistory =
                accountService.findTransactionHistory(page, size, transactionType, accountNumber);

        return transactionHistory.map(AccountHistoryResponse::from);
    }


}
