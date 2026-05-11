package com.nistra.demy.platform.scheduling.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ClassroomId(Long id) {
    /**
     * Default constructor
     */
    public ClassroomId() {
        this(null);
    }

    /**
     * Constructor with validation
     * @param id Classroom ID
     */
    public ClassroomId {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Classroom ID must be a positive number");
        }
    }
}
