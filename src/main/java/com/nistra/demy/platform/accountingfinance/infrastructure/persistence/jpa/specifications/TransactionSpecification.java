package com.nistra.demy.platform.accountingfinance.infrastructure.persistence.jpa.specifications;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionCategory;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionMethod;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecification {

    private TransactionSpecification() {}

    public static Specification<Transaction> hasAcademyId(AcademyId academyId) {
        return (root, query, cb) -> cb.equal(root.get("academyId"), academyId);
    }

    public static Specification<Transaction> hasType(TransactionType type) {
        return (root, query, cb) -> cb.equal(root.get("transactionType"), type);
    }

    public static Specification<Transaction> hasCategory(TransactionCategory category) {
        return (root, query, cb) -> cb.equal(root.get("transactionCategory"), category);
    }

    public static Specification<Transaction> hasMethod(TransactionMethod method) {
        return (root, query, cb) -> cb.equal(root.get("transactionMethod"), method);
    }
}
