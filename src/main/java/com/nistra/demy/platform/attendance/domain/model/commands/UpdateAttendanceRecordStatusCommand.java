package com.nistra.demy.platform.attendance.domain.model.commands;

import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceStatus;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;

/**
 * UpdateAttendanceRecordStatusCommand
 * @summary
 * UpdateAttendanceRecordStatusCommand is a record class that represents the command to update the status of an attendance record of a class attendance.
 * @param classAttendanceId
 * @param dni
 * @param newStatus
 */
public record UpdateAttendanceRecordStatusCommand(
        Long classAttendanceId,
        DniNumber dni,
        AttendanceStatus newStatus
) {
    public UpdateAttendanceRecordStatusCommand {
        if (classAttendanceId == null) throw new IllegalArgumentException("id is required");
        if (dni == null) throw new IllegalArgumentException("dni is required");
        if (newStatus == null) throw new IllegalArgumentException("status is required");
    }
}
