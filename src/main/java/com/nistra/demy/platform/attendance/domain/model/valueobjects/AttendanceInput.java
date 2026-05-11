package com.nistra.demy.platform.attendance.domain.model.valueobjects;

import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;

/**
 * Value Object representing a plain data input for attendance.
 * Because in the command cant have a list of entities
 * @author Diego Vilca
 * @param dni
 * @param status
 */
public record AttendanceInput(DniNumber dni, AttendanceStatus status ) {

    public AttendanceInput {
        if(dni == null) {
            throw new IllegalArgumentException("Dni is required");
        }
        if(status == null) {
            throw new IllegalArgumentException("Status is required");
        }
    }

}
