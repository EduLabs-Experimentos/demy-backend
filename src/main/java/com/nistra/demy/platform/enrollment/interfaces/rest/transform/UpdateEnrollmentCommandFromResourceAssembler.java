package com.nistra.demy.platform.enrollment.interfaces.rest.transform;

import com.nistra.demy.platform.enrollment.domain.model.commands.UpdateEnrollmentCommand;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.EnrollmentStatus;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.PaymentStatus;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.UpdateEnrollmentResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;

import java.math.BigDecimal;
import java.util.Currency;

public class UpdateEnrollmentCommandFromResourceAssembler {
    public static UpdateEnrollmentCommand toCommandFromResource(Long enrollmentId, UpdateEnrollmentResource resource) {
        return new UpdateEnrollmentCommand(
                enrollmentId,
                new Money(
                        new BigDecimal(resource.amount()),
                        Currency.getInstance(resource.currency())
                ),
                EnrollmentStatus.valueOf(
                        resource.enrollmentStatus().toUpperCase()
                ),
                PaymentStatus.valueOf(
                        resource.paymentStatus().toUpperCase()
                )
        );
    }
}
