package com.nistra.demy.platform.institution.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

/**
 * Value object representing the description of an academy.
 * <p>
 * Encapsulates the academy description ensuring it is not null or blank.
 * Used as an embeddable in the persistence layer.
 * It is marked as embeddable for use in JPA entities.
 *
 * @author Salim Ramirez
 * @see IllegalArgumentException
 * @since 1.0.0
 */
@Embeddable
public record AcademyDescription(
        @Column(nullable = false)
        @NotBlank
        String description
) {
    /**
     * Default constructor required by JPA.
     * Initializes the description with a default empty string.
     */
    public AcademyDescription() {
        this("");
    }

    /**
     * Constructs an AcademyDescription with the specified description.
     *
     * @param description the description of the academy
     * @throws IllegalArgumentException if description is null or blank
     */
    public AcademyDescription {
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Academy description cannot be null or empty");
    }
}
