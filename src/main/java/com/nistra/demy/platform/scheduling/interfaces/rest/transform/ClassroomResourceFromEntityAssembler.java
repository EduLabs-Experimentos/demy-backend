package com.nistra.demy.platform.scheduling.interfaces.rest.transform;

import com.nistra.demy.platform.scheduling.domain.model.aggregates.Classroom;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.ClassroomResource;
import org.springframework.stereotype.Component;


@Component // NECESARIO
public class ClassroomResourceFromEntityAssembler {
    /**
     * Converts a Classroom entity to a ClassroomResource
     * @param entity the Classroom entity
     * @return the ClassroomResource
     */
    public static ClassroomResource toResourceFromEntity(Classroom entity) {
        return new ClassroomResource(
                entity.getId(),
                entity.getCode(),
                entity.getCapacity(),
                entity.getCampus()
        );
    }
}