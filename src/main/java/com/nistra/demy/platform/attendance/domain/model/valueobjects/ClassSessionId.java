package com.nistra.demy.platform.attendance.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object that represents the Id of a ClassSession
 * @param classSessionId
 */
@Embeddable
public record ClassSessionId(
        Long classSessionId
) {
    /**
     * Default constructor required by JPA.
     * Initializes the classSessionID with a default value of 0.
     */
    public ClassSessionId() {
        this(null);
    }

    /**
     * Constructs an ClassSessionId with the specified ID.
     *
     * @param classSessionId the unique identifier for the academy
     * @throws IllegalArgumentException if academyId is less than or equal to zero
     */
    public ClassSessionId {
        if (classSessionId != null && classSessionId <= 0)
            throw new IllegalArgumentException("ClassSession ID must be greater than zero");
    }
    public Long value() { return classSessionId; }
}
