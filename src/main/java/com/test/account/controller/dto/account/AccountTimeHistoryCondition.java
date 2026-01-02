package com.test.account.controller.dto.account;

import com.test.account.controller.dto.time.TimePreset;
import com.test.account.service.domain.enumtype.TransactionType;
import lombok.Data;

@Data
public class AccountTimeHistoryCondition {

    private TimePreset preset;

    private String type;

}
