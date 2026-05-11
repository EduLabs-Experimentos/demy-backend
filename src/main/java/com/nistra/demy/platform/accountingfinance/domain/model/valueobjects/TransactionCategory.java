package com.nistra.demy.platform.accountingfinance.domain.model.valueobjects;

import com.nistra.demy.platform.accountingfinance.domain.exceptions.InvalidTransactionCategoryException;

public enum TransactionCategory {
    STUDENT_ENROLLMENT,
    STUDENT_MONTHLY_FEE,
    STUDENT_ONE_TIME_PAYMENT,
    TEACHER_SALARY,
    OFFICE_SUPPLIES,
    OTHER;

    public static TransactionCategory fromString(String category) {
        if (category == null || category.isBlank())
            throw new IllegalArgumentException("Category cannot be null or blank");

        String normalized = category.trim()
                .replace("-", "_")
                .replace(" ", "_")
                .toUpperCase();

        for (TransactionCategory tc : TransactionCategory.values()) {
            if (tc.name().equalsIgnoreCase(normalized)) {
                return tc;
            }
        }
        throw new InvalidTransactionCategoryException(category);
    }
}
