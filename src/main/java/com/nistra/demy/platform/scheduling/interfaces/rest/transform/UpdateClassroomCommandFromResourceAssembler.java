package com.nistra.demy.platform.scheduling.interfaces.rest.transform;

import com.nistra.demy.platform.scheduling.domain.model.commands.UpdateClassroomCommand;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.UpdateClassroomResource;

public class UpdateClassroomCommandFromResourceAssembler {
    /**
     * Converts an UpdateClassroomResource to an UpdateClassroomCommand
     * @param classroomId the classroom ID
     * @param resource the UpdateClassroomResource
     * @return the UpdateClassroomCommand
     */
    public static UpdateClassroomCommand toCommandFromResource(Long classroomId, UpdateClassroomResource resource) {
        return new UpdateClassroomCommand(
                classroomId,
                resource.code(),
                resource.capacity(),
                resource.campus()
        );
    }
}