package com.nistra.demy.platform.enrollment.interfaces.rest.resources;

import com.nistra.demy.platform.enrollment.domain.model.valueobjects.EnrollmentStatus;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.PaymentStatus;

import java.math.BigDecimal;
import java.util.Currency;

public record EnrollmentResource (
        Long id,
        Long studentId,
        Long periodId,
        Long scheduleId,
        Long academyId,
        BigDecimal amount,
        Currency currency,
        String enrollmentStatus,
        String paymentStatus
) {
}