package com.nistra.demy.platform.accountingfinance.interfaces.rest.transform;

import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;

public class TransactionTypeFromResourceAssembler {
    public static TransactionType toValueObjectFromString(String type) {
        if (type == null || type.isBlank()) return null;
        return TransactionType.fromString(type);
    }
}
