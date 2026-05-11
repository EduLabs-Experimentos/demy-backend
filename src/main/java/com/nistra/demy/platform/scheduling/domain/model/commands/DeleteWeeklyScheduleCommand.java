package com.nistra.demy.platform.scheduling.domain.model.commands;

/**
 * Command to delete a weekly schedule.
 * @param weeklyScheduleId ID of the weekly schedule to delete
 */
public record DeleteWeeklyScheduleCommand (Long weeklyScheduleId){
    public DeleteWeeklyScheduleCommand {
        if (weeklyScheduleId == null || weeklyScheduleId <= 0) {
            throw new IllegalArgumentException("WeeklyScheduleId cannot be null or less than 1");
        }
    }
}
