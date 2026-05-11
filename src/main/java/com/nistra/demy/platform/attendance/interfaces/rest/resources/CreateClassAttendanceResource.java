package com.nistra.demy.platform.attendance.interfaces.rest.resources;

import java.time.LocalDate;
import java.util.List;

/**
 * Resource record for creating a class attendance.
 * @summary
 * This record represents the resource for creating a class attendance.
 * It contains the classSessionId, date and the list of attendance record resources.
 * @param classSessionId
 * @param date
 * @param attendance
 */

public record CreateClassAttendanceResource(Long classSessionId,
                                            LocalDate date,
                                            List<AttendanceRecordResource> attendance) {

}
