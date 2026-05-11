package com.nistra.demy.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

/**
 * Value object representing a person's full name.
 * <p>
 * Encapsulates first and last names with validation rules
 * ensuring they are non-blank, do not exceed 50 characters,
 * and do not contain spaces.
 *
 * @author Salim Ramirez
 * @see IllegalArgumentException
 * @since 1.0.0
 */
@Embeddable
public record PersonName(
        @Column(nullable = false, length = 50)
        @NotBlank
        String firstName,

        @Column(nullable = false, length = 50)
        @NotBlank
        String lastName
) {
    /**
     * Default constructor required by JPA.
     * Initializes first and last names as empty strings.
     */
    public PersonName() {
        this("", "");
    }

    /**
     * Constructs a FullName with validation.
     *
     * @param firstName the first name
     * @param lastName the last name
     * @throws IllegalArgumentException if firstName or lastName is null, blank,
     *                                 exceeds 50 characters, or contains spaces
     */
    public PersonName {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be null or blank");
        if (firstName.length() > 50)
            throw new IllegalArgumentException("First name cannot exceed 50 characters");
        if (firstName.contains(" "))
            throw new IllegalArgumentException("First name cannot contain spaces");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be null or blank");
        if (lastName.length() > 50)
            throw new IllegalArgumentException("Last name cannot exceed 50 characters");
        if (lastName.contains(" "))
            throw new IllegalArgumentException("Last name cannot contain spaces");
    }
}
