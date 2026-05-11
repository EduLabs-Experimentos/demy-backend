package com.nistra.demy.platform.accountingfinance.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class InvalidTransactionCategoryException extends DomainException {
    public InvalidTransactionCategoryException(String category) {
        super("Invalid transaction category %s".formatted(category), category);
    }
}
