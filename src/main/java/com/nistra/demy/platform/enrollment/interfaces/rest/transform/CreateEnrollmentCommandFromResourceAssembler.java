package com.nistra.demy.platform.enrollment.interfaces.rest.transform;

import com.nistra.demy.platform.enrollment.domain.model.commands.CreateEnrollmentCommand;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.*;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.CreateEnrollmentResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;

import java.math.BigDecimal;
import java.util.Currency;


public class CreateEnrollmentCommandFromResourceAssembler {
    public static CreateEnrollmentCommand toCommandFromResource(CreateEnrollmentResource resource) {
        return new CreateEnrollmentCommand(
                new StudentId(resource.studentId()),
                new PeriodId(resource.periodId()),
                new ScheduleId(resource.scheduleId()),
                new Money(
                        new BigDecimal(resource.amount()),
                        Currency.getInstance(resource.currency())
                ),
                PaymentStatus.valueOf(
                        resource.paymentStatus().toUpperCase()
                )
        );
    }
}
