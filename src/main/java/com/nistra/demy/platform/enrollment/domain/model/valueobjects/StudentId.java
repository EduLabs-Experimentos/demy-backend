package com.nistra.demy.platform.enrollment.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record StudentId(Long studentId) {

    public StudentId() {
        this(null);
    }

    public StudentId {
        if (studentId == null || studentId < 1) {
            throw new IllegalArgumentException("Student ID must not be null or less than 1");
        }
    }
}
