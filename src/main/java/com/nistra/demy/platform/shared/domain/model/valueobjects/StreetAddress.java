package com.nistra.demy.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value object representing a street address.
 * <p>
 * Encapsulates the street, district, province, and department details of an address.
 * Used as an embeddable in the persistence layer.
 *
 * @author Salim Ramirez
 * @see IllegalArgumentException
 * @since 1.0.0
 */
@Embeddable
public record StreetAddress(
        @Column(nullable = false, length = 100)
        String street,

        @Column(nullable = false, length = 50)
        String district,

        @Column(nullable = false, length = 50)
        String province,

        @Column(nullable = false, length = 50)
        String department
) {
    /**
     * Default constructor required by JPA.
     * Initializes all fields with empty strings.
     */
    public StreetAddress() {
        this("", "", "", "");
    }

    /**
     * Constructs a StreetAddress with the specified details.
     *
     * @param street     the street name and number
     * @param district   the district name
     * @param province   the province name
     * @param department the department name
     * @throws IllegalArgumentException if any field is null, empty, or exceeds its length constraints
     */
    public StreetAddress {
        if (street == null || street.isBlank())
            throw new IllegalArgumentException("Street cannot be null or empty");
        if (street.length() > 100)
            throw new IllegalArgumentException("Street cannot exceed 100 characters");
        if (district == null || district.isBlank())
            throw new IllegalArgumentException("District cannot be null or empty");
        if (district.length() > 50)
            throw new IllegalArgumentException("District cannot exceed 50 characters");
        if (province == null || province.isBlank())
            throw new IllegalArgumentException("Province cannot be null or empty");
        if (province.length() > 50)
            throw new IllegalArgumentException("Province cannot exceed 50 characters");
        if (department == null || department.isBlank())
            throw new IllegalArgumentException("Department cannot be null or empty");
        if (department.length() > 50)
            throw new IllegalArgumentException("Department cannot exceed 50 characters");
    }
}
