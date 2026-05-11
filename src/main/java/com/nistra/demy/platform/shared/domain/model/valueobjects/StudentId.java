package com.nistra.demy.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record StudentId(
        @Column(nullable = false)
        Long studentId
) {
    public StudentId() {
        this(null);
    }

    public StudentId {
        if (studentId == null || studentId <= 0)
            throw new IllegalArgumentException("Student ID must be greater than zero");
    }
}
