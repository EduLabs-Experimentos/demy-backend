package com.nistra.demy.platform.scheduling.interfaces.rest.transform;

import com.nistra.demy.platform.scheduling.domain.model.commands.UpdateCourseCommand;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.UpdateCourseResource;

public class UpdateCourseCommandFromResourceAssembler {
    /**
     * Converts an UpdateCourseResource to an UpdateCourseCommand
     * @param courseId the course ID
     * @param resource the UpdateCourseResource
     * @return the UpdateCourseCommand
     */
    public static UpdateCourseCommand toCommandFromResource(Long courseId, UpdateCourseResource resource) {
        return new UpdateCourseCommand(
                courseId,
                resource.name(),
                resource.code(),
                resource.description()
        );
    }
}