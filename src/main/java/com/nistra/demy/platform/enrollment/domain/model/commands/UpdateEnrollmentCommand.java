package com.nistra.demy.platform.enrollment.domain.model.commands;

import com.nistra.demy.platform.enrollment.domain.model.valueobjects.EnrollmentStatus;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.PaymentStatus;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;

import java.math.BigDecimal;
import java.util.Currency;

public record UpdateEnrollmentCommand(
        Long enrollmentId,
        Money money,
        EnrollmentStatus enrollmentStatus,
        PaymentStatus paymentStatus
) {
    public UpdateEnrollmentCommand {
        if (enrollmentId == null || enrollmentId < 1) {
            throw new IllegalArgumentException("enrollmentId cannot be null");
        }
        if (money == null || money.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("money cannot be negative");
        }
        if (enrollmentStatus == null) {
            throw new IllegalArgumentException("enrollmentStatus cannot be null");
        }
        if (paymentStatus == null) {
            throw new IllegalArgumentException("paymentStatus cannot be null");
        }
    }
}
