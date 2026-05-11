package com.nistra.demy.platform.scheduling.domain.model.commands;

/**
 * Command to update the details of an existing schedule.
 *
 * @param scheduleId The unique identifier of the schedule to be updated.
 * @param classroomId The unique identifier of the classroom for the schedule.
 * @param startTime The start time of the schedule.
 * @param endTime The end time of the schedule.
 * @param dayOfWeek The day of the week when the schedule occurs.
 */
public record UpdateScheduleCommand (
        Long scheduleId,
        Long classroomId,
        String startTime,
        String endTime,
        String dayOfWeek
) {
    public UpdateScheduleCommand {
        if(scheduleId == null || scheduleId <= 0) {
            throw new IllegalArgumentException("Schedule ID must be a positive number.");
        }
        if(classroomId == null || classroomId<= 0) {
            throw new IllegalArgumentException("Classroom Id must not be null or blank.");
        }
        if(startTime == null || startTime.isBlank()) {
            throw new IllegalArgumentException("StartTime  must not be null or blank.");
        }
        if(endTime == null || endTime.isBlank()) {
            throw new IllegalArgumentException("EndTime  must not be null or blank.");
        }
        if(dayOfWeek == null || dayOfWeek.isBlank()) {
            throw new IllegalArgumentException("DayOfWeek  must not be null or blank.");
        }
    }
}