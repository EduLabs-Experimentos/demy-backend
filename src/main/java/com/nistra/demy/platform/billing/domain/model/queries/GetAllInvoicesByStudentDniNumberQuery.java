package com.nistra.demy.platform.billing.domain.model.queries;

import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;

public record GetAllInvoicesByStudentDniNumberQuery(
        DniNumber dniNumber
) {
}
