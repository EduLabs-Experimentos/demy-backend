package com.nistra.demy.platform.enrollment.interfaces.rest.transform;

import com.nistra.demy.platform.enrollment.domain.model.commands.UpdateAcademicPeriodCommand;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.UpdateAcademicPeriodResource;

/**
 * Assembler to convert a UpdateAcademicPeriodResource to a UpdateAcademicPeriodCommand.
 */
public class UpdateAcademicPeriodCommandFromResourceAssembler {

    /**
     * Converts an UpdateAcademicPeriodResource to an UpdateAcademicPeriodCommand.
     *
     * @param resource The {@link UpdateAcademicPeriodResource} to convert.
     * @return The {@link UpdateAcademicPeriodCommand} command that results from the conversion.
     */
    public static UpdateAcademicPeriodCommand toCommandFromResource(Long academicPeriodId, UpdateAcademicPeriodResource resource) {
        // Convert the resource to a command, passing all necessary fields
        return new UpdateAcademicPeriodCommand(
                academicPeriodId,
                resource.periodName(),
                resource.startDate(),
                resource.endDate(),
                resource.isActive()
        );
    }
}
