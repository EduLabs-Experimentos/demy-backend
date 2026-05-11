package com.nistra.demy.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value object representing a phone number with country code and local number.
 * <p>
 * Encapsulates the country code and local number ensuring they adhere to specified validation rules.
 * Used as an embeddable in the persistence layer.
 *
 * @author Salim Ramirez
 * @see IllegalArgumentException
 * @since 1.0.0
 */
@Embeddable
public record PhoneNumber(
        @Column(nullable = false, length = 5)
        String countryCode,

        @Column(nullable = false, length = 15)
        String phone
) {
    /**
     * Default constructor for JPA
     * Initializes with empty strings
     */
    public PhoneNumber() {
        this("", "");
    }

    /**
     * Constructs a PhoneNumber with the specified country code and number.
     *
     * @param countryCode the country code, must be non-null, non-empty, max 5 characters, digits only with optional leading +
     * @param phone       the local phone number, must be non-null, non-empty, max 15 characters, digits only (spaces and dashes allowed)
     * @throws IllegalArgumentException if any validation rules are violated
     */
    public PhoneNumber {
        if (countryCode == null || countryCode.isBlank())
            throw new IllegalArgumentException("Country code cannot be null or empty");
        if (countryCode.length() > 5)
            throw new IllegalArgumentException("Country code cannot be longer than 5 characters");
        if (!countryCode.matches("^\\+?\\d{1,5}$"))
            throw new IllegalArgumentException("Invalid country code");
        if (phone == null || phone.isBlank())
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        if (phone.length() > 15)
            throw new IllegalArgumentException("Phone number cannot be longer than 15 characters");
        if (!phone.replaceAll("[\\s\\-]", "").matches("^\\d{6,12}$"))
            throw new IllegalArgumentException("Invalid local number");
    }

    /**
     * Returns the full phone number in E.164 format (e.g., +1234567890).
     *
     * @return the full phone number as a string
     */
    public String getFullNumber() {
        return countryCode.replace("+", "") + phone.replaceAll("[\\s\\-]", "");
    }

    /**
     * Returns the phone number formatted for display (e.g., +1 234-567-890).
     *
     * @return the formatted phone number as a string
     */
    public String getFormatted() {
        return String.format("%s %s", countryCode, phone);
    }
}
