package com.nistra.demy.platform.shared.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Value object representing an email address.
 * <p>
 * This class encapsulates the email address ensuring it is not null, not empty, and follows a valid format.
 * It is used as an embeddable in the persistence layer.
 *
 * @author Salim Ramirez
 * @see IllegalArgumentException
 * @since 1.0.0
 */
@Embeddable
public record EmailAddress(
        @Column(nullable = false, unique = true)
        String email
) {
    /**
     * Default constructor required by JPA.
     * Initializes the email address with an empty string.
     */
    public EmailAddress() {
        this("");
    }

    /**
     * Constructs an EmailAddress with the specified email.
     *
     * @param email the email address to be encapsulated
     * @throws IllegalArgumentException if email is null, empty, or does not match a valid email format
     */
    public EmailAddress {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email address cannot be null or empty");
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            throw new IllegalArgumentException("Invalid email address format");
    }
}
