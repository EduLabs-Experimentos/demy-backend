package com.nistra.demy.platform.institution.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

/**
 * Value object representing the name of an academy.
 * <p>
 * This class is used to encapsulate the academy name ensuring it is not null, not blank,
 * and does not exceed a specified length.
 * It is marked as embeddable for use in JPA entities.
 *
 * @author Salim Ramirez
 * @see IllegalArgumentException
 * @since 1.0.0
 */
@Embeddable
public record AcademyName(
        @Column(nullable = false, length = 80)
        @NotBlank
        String name
) {
    /**
     * Default constructor required by JPA.
     * Initializes the academy name with an empty string.
     */
    public AcademyName() {
        this("");
    }

    /**
     * Constructs an AcademyName with the specified name.
     *
     * @param name the name of the academy
     * @throws IllegalArgumentException if academyName is null, blank, or exceeds 80 characters
     */
    public AcademyName {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Academy name cannot be null or empty");
        if (name.length() > 80)
            throw new IllegalArgumentException("Academy name cannot exceed 80 characters");
    }
}
