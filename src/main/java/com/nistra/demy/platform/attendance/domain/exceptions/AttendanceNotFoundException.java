package com.nistra.demy.platform.attendance.domain.exceptions;

public class AttendanceNotFoundException extends RuntimeException {
    public AttendanceNotFoundException() {
        super("Assistance not found or outside your academy");
    }
}
