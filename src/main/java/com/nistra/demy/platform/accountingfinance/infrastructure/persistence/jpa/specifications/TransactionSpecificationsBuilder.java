package com.nistra.demy.platform.accountingfinance.infrastructure.persistence.jpa.specifications;

import com.nistra.demy.platform.accountingfinance.domain.model.aggregates.Transaction;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionCategory;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionMethod;
import com.nistra.demy.platform.accountingfinance.domain.model.valueobjects.TransactionType;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.stream.Stream;

import static com.nistra.demy.platform.accountingfinance.infrastructure.persistence.jpa.specifications.TransactionSpecification.hasAcademyId;

public class TransactionSpecificationsBuilder {

    private TransactionSpecificationsBuilder() {}

    public static Specification<Transaction> build(
            AcademyId academyId,
            TransactionType type,
            TransactionCategory category,
            TransactionMethod method
    ) {
        return Stream.of(
                        Optional.of(hasAcademyId(academyId)),
                        Optional.ofNullable(type).map(TransactionSpecification::hasType),
                        Optional.ofNullable(category).map(TransactionSpecification::hasCategory),
                        Optional.ofNullable(method).map(TransactionSpecification::hasMethod)
                )
                .flatMap(Optional::stream)
                .reduce(Specification::and)
                .orElse(null);
    }
}
