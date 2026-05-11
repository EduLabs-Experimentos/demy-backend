package com.nistra.demy.platform.enrollment.domain.model.aggregates;

import com.nistra.demy.platform.enrollment.domain.model.commands.CreateAcademicPeriodCommand;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.*;
import com.nistra.demy.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

/**
 * AcademicPeriod Aggregate Root
 */
@Getter
@Entity
@Table(name = "academic_periods")
public class AcademicPeriod extends AuditableAbstractAggregateRoot<AcademicPeriod> {

    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "period_name", nullable = false))
    })
    private String periodName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "start_date", nullable = false)),
            @AttributeOverride(name = "endDate", column = @Column(name = "end_date", nullable = false))
    })
    private PeriodDuration periodDuration;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "isActive", column = @Column(name = "is_active", nullable = false))
    })
    private ActiveStatus isActive;

    @Embedded
    @Getter
    private AcademyId academyId;


    /**
     * Default constructor for JPA
     */
    public AcademicPeriod() {}


    /**
     * Constructor for creating a new AcademicPeriod
     * @param periodName The name of the academic period
     * @param academyId The academy identifier
     * @param startDate The start date of the period
     * @param endDate The end date of the period
     */
    private AcademicPeriod(String periodName, LocalDate startDate, LocalDate endDate, Boolean isActive,  AcademyId academyId) {
        this();
        this.periodName = periodName;
        this.periodDuration = new PeriodDuration(startDate, endDate);
        this.isActive = new ActiveStatus(isActive);
        this.academyId = academyId;
    }

    public AcademicPeriod updateInformation(String periodName, LocalDate startDate, LocalDate endDate, Boolean activeStatus) {
        this.periodName = periodName;
        this.periodDuration = new PeriodDuration(startDate, endDate);
        this.isActive = new ActiveStatus(activeStatus);
        return this;
    }

    public static AcademicPeriod createActiveAcademicPeriod(
            String periodName,
            LocalDate startDate,
            LocalDate endDate,
            AcademyId academyId
    ) {
        return new AcademicPeriod(
                periodName,
                startDate,
                endDate,
                true,
                academyId
        );
    }
}

