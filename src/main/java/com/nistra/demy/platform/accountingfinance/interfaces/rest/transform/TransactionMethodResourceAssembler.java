package com.nistra.demy.platform.accountingfinance.interfaces.rest.transform;

import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionMethod;

public class TransactionMethodResourceAssembler {
    public static TransactionMethod toValueObjectFromString(String method) {
        if (method == null || method.isBlank()) return null;
        return TransactionMethod.fromString(method);
    }
}
