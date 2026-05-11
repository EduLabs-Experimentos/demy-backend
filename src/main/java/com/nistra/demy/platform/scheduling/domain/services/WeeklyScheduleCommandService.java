package com.nistra.demy.platform.scheduling.domain.services;

import com.nistra.demy.platform.scheduling.domain.model.aggregates.WeeklySchedule;
import com.nistra.demy.platform.scheduling.domain.model.commands.*;
import com.nistra.demy.platform.scheduling.domain.model.entities.Schedule;

import java.util.Optional;

public interface WeeklyScheduleCommandService {

    Long handle(CreateWeeklyScheduleCommand command);

    Optional<WeeklySchedule> handle(UpdateWeeklyScheduleNameCommand command);

    Optional<WeeklySchedule> handle(AddScheduleToWeeklyCommand command);

    Optional<WeeklySchedule> handle(RemoveScheduleFromWeeklyCommand command);

    void handle(DeleteWeeklyScheduleCommand command);

    Optional<Schedule> handle(UpdateScheduleCommand command);

}