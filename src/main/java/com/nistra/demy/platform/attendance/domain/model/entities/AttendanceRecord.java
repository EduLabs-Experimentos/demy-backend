package com.nistra.demy.platform.attendance.domain.model.entities;


import com.nistra.demy.platform.attendance.domain.model.aggregates.ClassAttendance;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceStatus;
import com.nistra.demy.platform.shared.domain.model.entities.AuditableModel;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

/**
 * AttendanceRecord entity
 * @author Diego Vilca
 * @summary
 * Represents the attendance of a single student in a class session,
 * linking the student's identifier (DNI) and their attendance status.
 * This entity is part of the Attendance bounded context.
 *
 */

@Getter
@Entity
public class AttendanceRecord extends AuditableModel {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "class_attendance_id", nullable = false)
    private ClassAttendance classAttendance;

    @Embedded
    @AttributeOverride(
            name = "dniNumber",
            column = @Column(name = "dni_number", nullable = false, unique = false)
    )
    private DniNumber dni;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status ;


    /**
     * Required by JPA
     */
    public AttendanceRecord() {}


    public AttendanceRecord(ClassAttendance parent, DniNumber dni, AttendanceStatus status) {
        this.classAttendance = Objects.requireNonNull(parent);
        this.dni = Objects.requireNonNull(dni);
        this.status = Objects.requireNonNull(status);
    }

    public void updateStatus(AttendanceStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus);
    }

}
