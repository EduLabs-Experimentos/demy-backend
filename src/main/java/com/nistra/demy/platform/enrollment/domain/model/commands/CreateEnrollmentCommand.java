package com.nistra.demy.platform.enrollment.domain.model.commands;

import com.nistra.demy.platform.enrollment.domain.model.valueobjects.*;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;

public record CreateEnrollmentCommand(
        StudentId studentId,
        PeriodId periodId,
        ScheduleId scheduleId,
        Money money,
        PaymentStatus paymentStatus
) {
    public CreateEnrollmentCommand {
        if (studentId == null) {
            throw new IllegalArgumentException("StudentId cannot be null");
        }
        if (periodId == null) {
            throw new IllegalArgumentException("PeriodId cannot be null");
        }
        if (scheduleId == null) {
            throw new IllegalArgumentException("scheduleId cannot be null");
        }
        if (money == null) {
            throw new IllegalArgumentException("Money cannot be null");
        }
        if (paymentStatus == null) {
            throw new IllegalArgumentException("paymentStatus cannot be null");
        }
    }
}
