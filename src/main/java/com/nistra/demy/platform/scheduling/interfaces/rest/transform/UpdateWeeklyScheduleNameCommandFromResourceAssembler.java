package com.nistra.demy.platform.scheduling.interfaces.rest.transform;

import com.nistra.demy.platform.scheduling.domain.model.commands.UpdateWeeklyScheduleNameCommand;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.UpdateWeeklyScheduleNameResource;

public class UpdateWeeklyScheduleNameCommandFromResourceAssembler {
    /**
     * Converts an UpdateWeeklyScheduleNameResource to an UpdateWeeklyScheduleNameCommand
     * @param weeklyScheduleId the weekly schedule ID
     * @param resource the UpdateWeeklyScheduleNameResource
     * @return the UpdateWeeklyScheduleNameCommand
     */
    public static UpdateWeeklyScheduleNameCommand toCommandFromResource(Long weeklyScheduleId, UpdateWeeklyScheduleNameResource resource) {
        return new UpdateWeeklyScheduleNameCommand(weeklyScheduleId, resource.name());
    }
}