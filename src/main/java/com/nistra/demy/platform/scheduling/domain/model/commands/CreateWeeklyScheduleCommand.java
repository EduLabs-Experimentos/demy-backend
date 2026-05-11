package com.nistra.demy.platform.scheduling.domain.model.commands;

/**
 * Command to create a new weekly schedule.
 * @param name Name of the weekly schedule
 */
public record CreateWeeklyScheduleCommand(String name) {

    public CreateWeeklyScheduleCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
    }

}