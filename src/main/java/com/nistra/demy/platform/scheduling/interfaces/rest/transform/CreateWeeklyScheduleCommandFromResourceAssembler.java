package com.nistra.demy.platform.scheduling.interfaces.rest.transform;

import com.nistra.demy.platform.scheduling.domain.model.commands.CreateWeeklyScheduleCommand;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.CreateWeeklyScheduleResource;

public class CreateWeeklyScheduleCommandFromResourceAssembler {
    /**
     * Converts a CreateWeeklyScheduleResource to a CreateWeeklyScheduleCommand
     * @param resource the CreateWeeklyScheduleResource
     * @return the CreateWeeklyScheduleCommand
     */
    public static CreateWeeklyScheduleCommand toCommandFromResource(CreateWeeklyScheduleResource resource) {
        return new CreateWeeklyScheduleCommand(resource.name());
    }
}