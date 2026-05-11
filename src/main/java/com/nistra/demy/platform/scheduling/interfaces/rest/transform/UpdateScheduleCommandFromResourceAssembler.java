package com.nistra.demy.platform.scheduling.interfaces.rest.transform;

import com.nistra.demy.platform.scheduling.domain.model.commands.UpdateScheduleCommand;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.UpdateScheduleResource;

public class UpdateScheduleCommandFromResourceAssembler {
    public static UpdateScheduleCommand toCommandFromResource(Long scheduleId, UpdateScheduleResource resource) {
        return new UpdateScheduleCommand(
            scheduleId,
            resource.classroomId(),
            resource.startTime(),
            resource.endTime(),
            resource.dayOfWeek()
        );
    }
}
