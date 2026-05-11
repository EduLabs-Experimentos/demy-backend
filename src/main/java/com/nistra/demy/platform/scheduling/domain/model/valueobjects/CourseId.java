package com.nistra.demy.platform.scheduling.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record CourseId(Long id) {
    /**
     * Default constructor
     */
    public CourseId() {
        this(null);
    }

    /**
     * Constructor with validation
     * @param id Course ID
     */
    public CourseId {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Course ID must be a positive number");
        }
    }
}
