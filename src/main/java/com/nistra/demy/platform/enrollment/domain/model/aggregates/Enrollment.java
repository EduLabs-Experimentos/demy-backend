package com.nistra.demy.platform.enrollment.domain.model.aggregates;

import com.nistra.demy.platform.enrollment.domain.model.events.StudentEnrolledEvent;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.*;
import com.nistra.demy.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
public class Enrollment extends AuditableAbstractAggregateRoot<Enrollment> {

    @Embedded
    @Getter
    private StudentId studentId;

    @Embedded
    @Getter
    private PeriodId periodId;

    @Embedded
    @Getter
    private ScheduleId scheduleId;

    @Embedded
    @Getter
    private AcademyId academyId;

    @Embedded
    @Getter
    private Money money;

    @Enumerated(EnumType.STRING)
    @Getter
    private EnrollmentStatus enrollmentStatus;

    @Enumerated(EnumType.STRING)
    @Getter
    private PaymentStatus paymentStatus;

    /**
     * Default constructor for JPA
     */
    public Enrollment() {}

    /**
     * Constructor for creating a new Enrollment
     *
     * @param studentId The ID of the student
     * @param periodId The ID of the academic period
     * @param scheduleId The ID of the schedule
     * @param academyId The ID of the academy
     * @param money The payment for enrollment
     * @param enrollmentStatus The enrollment status
     * @param paymentStatus The payment status
     */
    private Enrollment(
            StudentId studentId,
            PeriodId periodId,
            ScheduleId scheduleId,
            AcademyId academyId,
            Money money,
            EnrollmentStatus enrollmentStatus,
            PaymentStatus paymentStatus
    ) {
        this();
        this.studentId = studentId;
        this.periodId = periodId;
        this.scheduleId = scheduleId;
        this.academyId = academyId;
        this.money = money;
        this.enrollmentStatus = enrollmentStatus;
        this.paymentStatus = paymentStatus;
    }

    /**
     * Update enrollment information
     */
    public Enrollment updateInformation(Money money, EnrollmentStatus enrollmentStatus, PaymentStatus paymentStatus) {
        this.money = money;
        this.enrollmentStatus = enrollmentStatus;
        this.paymentStatus = paymentStatus;
        return this;
    }


    public static Enrollment createEnrollmentActive(
            StudentId studentId,
            PeriodId periodId,
            ScheduleId scheduleId,
            AcademyId academyId,
            Money money,
            PaymentStatus paymentStatus
    ) {
        if (money.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Error: Enrollment amount must be positive");

        return new Enrollment(
                studentId,
                periodId,
                scheduleId,
                academyId,
                money,
                EnrollmentStatus.ACTIVE,
                paymentStatus
        );
    }

    public void registerEnrollmentEvent(DniNumber dniNumber) {
        addDomainEvent(new StudentEnrolledEvent(
                this,
                this.studentId.studentId(),
                dniNumber.dniNumber()
        ));
    }
}