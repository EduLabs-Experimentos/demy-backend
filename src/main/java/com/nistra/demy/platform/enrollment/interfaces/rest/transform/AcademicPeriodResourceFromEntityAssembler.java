package com.nistra.demy.platform.enrollment.interfaces.rest.transform;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.AcademicPeriod;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.AcademicPeriodResource;

/**
 * Assembler to convert an AcademicPeriod entity to an AcademicPeriodResource.
 */
public class AcademicPeriodResourceFromEntityAssembler {

    /**
     * Converts an AcademicPeriod entity to an AcademicPeriodResource.
     *
     * @param entity The {@link AcademicPeriod} entity to convert.
     * @return The {@link AcademicPeriodResource} resource that results from the conversion.
     */
    public static AcademicPeriodResource toResourceFromEntity(AcademicPeriod entity) {
        return new AcademicPeriodResource(
                entity.getId(),
                entity.getPeriodName(),
                entity.getPeriodDuration().startDate(),
                entity.getPeriodDuration().endDate(),
                entity.getIsActive().isActive(),
                entity.getAcademyId().academyId()
        );
    }
}
