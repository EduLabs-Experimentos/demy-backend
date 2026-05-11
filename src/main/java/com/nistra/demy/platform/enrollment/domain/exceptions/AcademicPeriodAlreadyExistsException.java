package com.nistra.demy.platform.enrollment.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class AcademicPeriodAlreadyExistsException extends DomainException {
    public AcademicPeriodAlreadyExistsException(String periodName) {
        super("Academic period with name %s already exists in the current academy context."
                .formatted(periodName), periodName);
    }
}