package com.test.account.service.domain.enumtype;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionType {
    TRANSFER_IN("수취"),
    TRANSFER_OUT("송금"),
    DEPOSIT("입금"),
    WITHDRAW("출금");

    private final String description;
    TransactionType(String description) { this.description = description; }

    @JsonCreator
    public static TransactionType fromDescription(String description) {
        for (TransactionType type : TransactionType.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        return null;
    }
}