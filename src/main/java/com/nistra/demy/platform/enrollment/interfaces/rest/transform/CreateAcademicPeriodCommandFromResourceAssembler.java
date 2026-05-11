package com.nistra.demy.platform.enrollment.interfaces.rest.transform;

import com.nistra.demy.platform.enrollment.domain.model.commands.CreateAcademicPeriodCommand;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.CreateAcademicPeriodResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;

/**
 * Assembler to convert a CreateAcademicPeriodResource to a CreateAcademicPeriodCommand.
 */
public class CreateAcademicPeriodCommandFromResourceAssembler {

    /**
     * Converts a CreateAcademicPeriodResource to a CreateAcademicPeriodCommand.
     *
     * @param resource The {@link CreateAcademicPeriodResource} to convert.
     * @return The {@link CreateAcademicPeriodCommand} command that results from the conversion.
     */
    public static CreateAcademicPeriodCommand toCommandFromResource(CreateAcademicPeriodResource resource) {
        return new CreateAcademicPeriodCommand(
                resource.periodName(),
                resource.startDate(),
                resource.endDate()
        );
    }
}
