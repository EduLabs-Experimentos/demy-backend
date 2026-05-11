package com.nistra.demy.platform.scheduling.interfaces.rest.transform;

import com.nistra.demy.platform.scheduling.domain.model.commands.CreateClassroomCommand;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.CreateClassroomResource;

public class CreateClassroomCommandFromResourceAssembler {
    /**
     * Converts a CreateClassroomResource to a CreateClassroomCommand
     * @param resource the CreateClassroomResource
     * @return the CreateClassroomCommand
     */
    public static CreateClassroomCommand toCommandFromResource(CreateClassroomResource resource) {
        return new CreateClassroomCommand(
                resource.code(),
                resource.capacity(),
                resource.campus()
        );
    }
}