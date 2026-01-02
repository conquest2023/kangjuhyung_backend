package com.test.account.controller;


import com.test.account.controller.dto.transfer.*;
import com.test.account.service.TransferService;
import com.test.account.service.domain.Cashier;
import com.test.account.service.record.TransferResult;
import com.test.account.service.record.WithdrawResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService cashierService;

    @PostMapping("/accounts/deposits")
    public ResponseEntity<DepositResponse> deposit(
            @Valid @RequestBody DepositRequest req
    ) {
        Cashier cashier = cashierService.deposit(
                req.accountNumber(),
                req.money()
        );

        return ResponseEntity.ok(
                DepositResponse.from(cashier)
        );
    }
    @PostMapping("/accounts/withdrawals")
    public ResponseEntity<WithdrawResponse> withdraw(
            @Valid @RequestBody WithdrawRequest req
    ) {
        WithdrawResult result = cashierService.withdraw(req.accountNumber(), req.money());
        return ResponseEntity.ok(
                WithdrawResponse.from(result)
        );
    }


    @PostMapping("/transfers")
    public ResponseEntity<TransferResponse> transfer(
            @Valid @RequestBody TransferRequest req
    ) {
        TransferResult result = cashierService.transfer(
                req.fromAccountNumber(),
                req.toAccountNumber(),
                req.amount()
        );
        
        return ResponseEntity.ok(
                TransferResponse.from(
                        result,
                        req.fromAccountNumber(),
                        req.toAccountNumber()
                )
        );
    }

}
