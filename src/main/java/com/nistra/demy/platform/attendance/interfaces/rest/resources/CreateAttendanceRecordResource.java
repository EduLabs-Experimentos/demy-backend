package com.nistra.demy.platform.attendance.interfaces.rest.resources;

/**
 * Resource record for creating an attendance record
 * @summary
 * This record represents the resource for creating a attendance record
 * It contains the dni of the student and his status.
 * @param dni
 * @param status
 */
public record CreateAttendanceRecordResource(String dni, String status) {
}
