package com.nistra.demy.platform.scheduling.domain.model.commands;

/**
 * Command to remove a schedule from a weekly schedule by their unique identifiers.
 *
 * @param weeklyScheduleId The unique identifier of the weekly schedule.
 * @param scheduleId The unique identifier of the schedule to be removed from the weekly schedule.
 */
public record RemoveScheduleFromWeeklyCommand(
        Long weeklyScheduleId,
        Long scheduleId
) {
    public RemoveScheduleFromWeeklyCommand {
        if (weeklyScheduleId == null || weeklyScheduleId <= 0) {
            throw new IllegalArgumentException("WeeklyScheduleId cannot be null or less than 1");
        }
        if(scheduleId == null || scheduleId <= 0) {
            throw new IllegalArgumentException("ScheduleId cannot be null or less than 1");
        }

    }
}