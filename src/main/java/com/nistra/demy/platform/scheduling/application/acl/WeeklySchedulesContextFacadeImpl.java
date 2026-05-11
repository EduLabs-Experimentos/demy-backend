package com.nistra.demy.platform.scheduling.application.acl;

import com.nistra.demy.platform.scheduling.domain.model.queries.GetWeeklyScheduleByIdQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetWeeklyScheduleByNameQuery;
import com.nistra.demy.platform.scheduling.domain.services.WeeklyScheduleQueryService;
import com.nistra.demy.platform.scheduling.interfaces.acl.WeeklySchedulesContextFacade;
import org.springframework.stereotype.Service;

/**
 * Weekly Schedules Context Facade Implementation
 * <p>This class provides a simplified interface for accessing weekly schedule services from other bounded contexts, implementing the Anti-Corruption Layer (ACL) pattern.</p>
 */
@Service
public class WeeklySchedulesContextFacadeImpl implements WeeklySchedulesContextFacade {

    private final WeeklyScheduleQueryService weeklyScheduleQueryService;

    /**
     * Constructor that initializes the facade with the required query service.
     *
     * @param weeklyScheduleQueryService The weekly schedule query service.
     */
    public WeeklySchedulesContextFacadeImpl(WeeklyScheduleQueryService weeklyScheduleQueryService) {
        this.weeklyScheduleQueryService = weeklyScheduleQueryService;
    }

    /**
     * This method is used to fetch a weekly schedule ID by its name.
     *
     * @param name The name of the weekly schedule to search for.
     * @return The ID of the weekly schedule if found, otherwise 0L.
     * @see GetWeeklyScheduleByNameQuery
     * @see WeeklyScheduleQueryService
     */
    @Override
    public Long fetchWeeklyScheduleIdByName(String name) {
        var getWeeklyScheduleByNameQuery = new GetWeeklyScheduleByNameQuery(name);
        var weeklySchedule = weeklyScheduleQueryService.handle(getWeeklyScheduleByNameQuery);
        return weeklySchedule.isEmpty() ? Long.valueOf(0L) : weeklySchedule.get().getId();
    }


    @Override
    public boolean existsScheduleById(Long scheduleId) {
        var query = new GetWeeklyScheduleByIdQuery(scheduleId);
        var schedule = weeklyScheduleQueryService.handle(query);
        return schedule.isPresent();
    }
}