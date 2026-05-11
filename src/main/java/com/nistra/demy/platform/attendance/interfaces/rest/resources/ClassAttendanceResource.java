package com.nistra.demy.platform.attendance.interfaces.rest.resources;

import java.time.LocalDate;
import java.util.List;

/**
 * Resource record for a class attendance.
 * @summary
 * This record represents the resource for a class attendance
 * It contains the id, classSessionId, date and attendance
 * @param id
 * @param classSessionId
 * @param date
 * @param attendance
 */
public record ClassAttendanceResource(Long id,
                                      Long classSessionId,
                                      LocalDate date,
                                      List<AttendanceRecordResource> attendance)  {
}
