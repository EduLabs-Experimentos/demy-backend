package com.nistra.demy.platform.enrollment.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class AcademicPeriodNotFoundException extends DomainException {
    public AcademicPeriodNotFoundException(Long periodId) {
        super("Academic period with id %s not found or does not belong to the current academy context."
                .formatted(periodId), periodId);
    }
}