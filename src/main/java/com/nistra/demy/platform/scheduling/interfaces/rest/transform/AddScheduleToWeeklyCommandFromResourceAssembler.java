package com.nistra.demy.platform.scheduling.interfaces.rest.transform;

import com.nistra.demy.platform.scheduling.domain.model.commands.AddScheduleToWeeklyCommand;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.AddScheduleToWeeklyResource;

public class AddScheduleToWeeklyCommandFromResourceAssembler {
    /**
     * Converts an AddScheduleToWeeklyResource to an AddScheduleToWeeklyCommand
     * @param weeklyScheduleId the weekly schedule ID
     * @param resource the AddScheduleToWeeklyResource
     * @return the AddScheduleToWeeklyCommand
     */
    public static AddScheduleToWeeklyCommand toCommandFromResource(Long weeklyScheduleId, AddScheduleToWeeklyResource resource) {
        return new AddScheduleToWeeklyCommand(
                weeklyScheduleId,
                resource.startTime(),
                resource.endTime(),
                resource.dayOfWeek(),
                resource.courseId(),
                resource.classroomId(),
                resource.teacherFirstName(),
                resource.teacherLastName()
        );
    }
}