package com.nistra.demy.platform.scheduling.interfaces.rest.resources;

public record UpdateScheduleResource(
        Long classroomId,
        String startTime,
        String endTime,
        String dayOfWeek

){
    public UpdateScheduleResource {
        if (classroomId == null || classroomId <= 0) {
            throw new IllegalArgumentException("ClassroomId must be a positive number.");
        }
        if (startTime == null || startTime.isBlank()) {
            throw new IllegalArgumentException("StartTime is required.");
        }
        if (endTime == null || endTime.isBlank()) {
            throw new IllegalArgumentException("EndTime is required.");
        }
        if (dayOfWeek == null || dayOfWeek.isBlank()) {
            throw new IllegalArgumentException("DayOfWeek is required.");
        }
    }
}
