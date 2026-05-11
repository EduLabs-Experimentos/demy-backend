package com.nistra.demy.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Represents a DNI (Documento Nacional de Identidad) number as a value object.
 * <p>
 * This class is used to encapsulate the logic and validation for a valid DNI number.
 * It is marked as embeddable for use in JPA entities.
 *
 * @author Salim Ramirez
 * @see IllegalArgumentException
 * @since 1.0.0
 */
@Embeddable
public record DniNumber(
        @Column(nullable = false, unique = true, length = 8)
        String dniNumber
) {
    /**
     * Default constructor for JPA.
     * This constructor initializes the DNI number to an empty string.
     */
    public DniNumber() {
        this("");
    }

    /**
     * Constructs a DniNumber with the specified DNI number.
     *
     * @param dniNumber the DNI number, must be exactly 8 digits long and not null or empty
     * @throws IllegalArgumentException if the DNI number is null, empty, not exactly 8 digits, or contains non-digit characters
     */
    public DniNumber {
        if (dniNumber == null || dniNumber.isBlank())
            throw new IllegalArgumentException("DNI number cannot be null or empty");
        if (dniNumber.length() != 8)
            throw new IllegalArgumentException("DNI number must be exactly 8 characters long");
        if (!dniNumber.matches("\\d+"))
            throw new IllegalArgumentException("DNI number must contain only digits");
    }
}
