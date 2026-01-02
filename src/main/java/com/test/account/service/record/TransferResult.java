package com.test.account.service.record;

import com.test.account.service.domain.Cashier;

public record TransferResult(
        Cashier cashier,
        String fromAccountNumber,
        String toAccountNumber,
        long todayTransferTotal
){
}
