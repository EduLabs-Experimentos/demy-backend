package com.nistra.demy.platform.scheduling.interfaces.rest.transform;

import com.nistra.demy.platform.scheduling.domain.model.aggregates.Course;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.CourseResource;
import org.springframework.stereotype.Component;

@Component // NECESARIO
public class CourseResourceFromEntityAssembler {
    /**
     * Converts a Course entity to a CourseResource
     * @param entity the Course entity
     * @return the CourseResource
     */
    public static CourseResource toResourceFromEntity(Course entity) {
        return new CourseResource(
                entity.getId(),
                entity.getName(),
                entity.getCode(),
                entity.getDescription()
        );
    }
}