package com.nistra.demy.platform.scheduling.domain.model.commands;

/**
 * Command to update the name of an existing weekly schedule.
 *
 * @param weeklyScheduleId The unique identifier of the weekly schedule to be updated.
 * @param name The new name to be assigned to the weekly schedule.
 */
public record UpdateWeeklyScheduleNameCommand(
        Long weeklyScheduleId,
        String name
) {
    public UpdateWeeklyScheduleNameCommand {
        if (weeklyScheduleId == null || weeklyScheduleId <= 0) {
            throw new IllegalArgumentException("weeklyScheduleId cannot be null or less than 1");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
    }
}