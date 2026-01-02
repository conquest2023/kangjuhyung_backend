package com.test.account.service.record;

import com.test.account.service.domain.Cashier;

public record WithdrawResult(
        Cashier cashier,
        long todayWithdrawTotal
) {}