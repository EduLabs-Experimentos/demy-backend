package com.nistra.demy.platform.enrollment.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.Currency;

public record UpdateEnrollmentResource(
        String amount,
        String currency,
        String enrollmentStatus,
        String paymentStatus
) {
}
