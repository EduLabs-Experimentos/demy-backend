package com.nistra.demy.platform.attendance.interfaces.rest.resources;

/**
 * Resource record for a attendance record
 * @summary
 * This record represents the resource for a class attendance
 * It contains dni and status
 * @param dni
 * @param status
 */
public record AttendanceRecordResource(String dni, String status) {

}
