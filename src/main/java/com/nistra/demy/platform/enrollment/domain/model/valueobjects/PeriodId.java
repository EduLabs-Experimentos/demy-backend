package com.nistra.demy.platform.enrollment.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record PeriodId(Long periodId) {

    public PeriodId() {
        this(null);
    }

    public PeriodId {
        if (periodId == null || periodId < 1) {
            throw new IllegalArgumentException("Period id must be greater than 0");
        }
    }
}
