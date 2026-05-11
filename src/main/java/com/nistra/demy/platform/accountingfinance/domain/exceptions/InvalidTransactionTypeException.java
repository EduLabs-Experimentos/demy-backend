package com.nistra.demy.platform.accountingfinance.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class InvalidTransactionTypeException extends DomainException {
    public InvalidTransactionTypeException(String type) {
        super("Invalid transaction type %s".formatted(type), type);
    }
}
