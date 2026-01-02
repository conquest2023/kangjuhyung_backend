package com.test.account.controller;


import com.test.account.controller.dto.account.AccountResponse;
import com.test.account.controller.dto.account.CreateAccountResponse;
import com.test.account.service.AccountService;
import com.test.account.service.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/accounts")
    public ResponseEntity<CreateAccountResponse> createAccount() {
        Account account = accountService.saveAccount();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/accounts/" + account.getAccountNumber())
                .body(CreateAccountResponse.from(account));
    }
    @GetMapping("/accounts/{accountNumber}")
        public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
            Account account = accountService.findAccountNumber(accountNumber);
            return ResponseEntity.ok(AccountResponse.from(account));

    }

    @DeleteMapping("/accounts/{accountNumber}")
    public ResponseEntity<?> closeAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.ok("계좌가 삭제되었습니다");
    }
}
