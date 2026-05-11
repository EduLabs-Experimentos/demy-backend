package com.nistra.demy.platform.scheduling.interfaces.acl;

/**
 * WeeklySchedulesContextFacade
 */
public interface WeeklySchedulesContextFacade {

    /**
     * Fetch a weekly schedule ID by its name.
     * @param name The name of the weekly schedule
     * @return The weekly schedule ID
     */
    Long fetchWeeklyScheduleIdByName(String name);

    boolean existsScheduleById(Long scheduleId);
}