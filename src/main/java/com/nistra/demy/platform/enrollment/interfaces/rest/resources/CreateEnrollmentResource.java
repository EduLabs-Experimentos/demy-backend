package com.nistra.demy.platform.enrollment.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.Currency;

public record CreateEnrollmentResource(
        Long studentId,
        Long periodId,
        Long scheduleId,
        String amount,
        String currency,
        String paymentStatus
) {
}
