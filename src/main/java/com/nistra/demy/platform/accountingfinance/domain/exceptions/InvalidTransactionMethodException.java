package com.nistra.demy.platform.accountingfinance.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class InvalidTransactionMethodException extends DomainException {
    public InvalidTransactionMethodException(String method) {
        super("Invalid transaction method %s".formatted(method), method);
    }
}
