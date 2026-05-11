package com.nistra.demy.platform.accountingfinance.interfaces.rest.transform;

import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionCategory;

public class TransactionCategoryFromResourceAssembler {
    public static TransactionCategory toValueObjectFromString(String category) {
        if (category == null || category.isBlank()) return null;
        return TransactionCategory.fromString(category);
    }
}
