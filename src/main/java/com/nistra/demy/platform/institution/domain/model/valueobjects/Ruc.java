package com.nistra.demy.platform.institution.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

/**
 * Value object representing the RUC (Registro Único de Contribuyentes),
 * a unique taxpayer registration number in Peru.
 * <p>
 * Enforces validation rules ensuring the RUC is non-blank,
 * exactly 11 digits long, and contains only numeric characters.
 *
 * @author Salim Ramirez
 * @see IllegalArgumentException
 * @since 1.0.0
 */
@Embeddable
public record Ruc(
        @Column(nullable = false, length = 11, unique = true)
        @NotBlank
        String ruc
) {
    /**
     * Default constructor required by JPA.
     * Initializes the RUC with an empty string.
     */
    public Ruc() {
        this("");
    }

    /**
     * Constructs a RUC with validation.
     *
     * @param ruc the RUC string
     * @throws IllegalArgumentException if the RUC is null, blank, not 11 characters long,
     *                                  or contains non-digit characters
     */
    public Ruc {
        if (ruc == null || ruc.isBlank())
            throw new IllegalArgumentException("RUC cannot be null or empty");
        if (ruc.length() != 11)
            throw new IllegalArgumentException("RUC must be 11 characters long");
        if (!ruc.matches("\\d{11}"))
            throw new IllegalArgumentException("RUC must contain only digits");
    }
}
