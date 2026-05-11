package com.nistra.demy.platform.attendance.domain.model.commands;


import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceInput;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.ClassSessionId;

import java.time.LocalDate;
import java.util.List;

/**
 * CreateClassAttendanceCommand
 * @summary
 * CreateClassAttendanceCommand is a record class that represents the command to create a class attendance.
 * @author diego
 * @param classSessionId
 * @param date
 * @param attendance
 */
public record CreateClassAttendanceCommand(
        ClassSessionId classSessionId,
        LocalDate date,
        List<AttendanceInput> attendance
) {
    /**
     * Validates the command
     * @throws IllegalArgumentException if classSessionId or date or attendance is null or empty
     */

    public CreateClassAttendanceCommand {
        if(classSessionId == null)
            throw new IllegalArgumentException("classSessionId cannot be null");
        if(date == null || date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("date cannot be null");
        if(attendance == null || attendance.isEmpty())
            throw new IllegalArgumentException("attendance cannot be null");
    }
}
