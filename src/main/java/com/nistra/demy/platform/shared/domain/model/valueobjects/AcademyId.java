package com.nistra.demy.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Value object representing the unique identifier of an academy in the shared domain.
 * <p>
 * Encapsulates the academy ID ensuring it is greater than zero.
 * Used as an embeddable in the persistence layer.
 *
 * @author Salim Ramirez
 * @see IllegalArgumentException
 * @since 1.0.0
 */
@Embeddable
public record AcademyId(
        Long academyId
) {
    /**
     * Default constructor required by JPA.
     * Initializes the academy ID with a default value of 0.
     */
    public AcademyId() {
        this(null);
    }

    /**
     * Constructs an AcademyId with the specified ID.
     *
     * @param academyId the unique identifier for the academy
     * @throws IllegalArgumentException if academyId is less than or equal to zero
     */
    public AcademyId {
        if (academyId != null && academyId <= 0)
            throw new IllegalArgumentException("Academy ID must be greater than zero");
    }
}
