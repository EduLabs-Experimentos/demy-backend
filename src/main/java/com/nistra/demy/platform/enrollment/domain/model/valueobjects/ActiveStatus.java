package com.nistra.demy.platform.enrollment.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * ActiveStatus Value Object
 */
@Embeddable
public record ActiveStatus(Boolean isActive) {
    /**
     * Default constructor with active status set to true
     */
    public ActiveStatus() {
        this(true);
    }

    /**
     * Constructor with validation
     * @param isActive The active status
     */
    public ActiveStatus {
        if (isActive == null) {
            throw new IllegalArgumentException("Active status must not be null");
        }
    }

    /**
     * Create an active status
     * @return ActiveStatus with active set to true
     */
    public static ActiveStatus active() {
        return new ActiveStatus(true);
    }

    /**
     * Create an inactive status
     * @return ActiveStatus with active set to false
     */
    public static ActiveStatus inactive() {
        return new ActiveStatus(false);
    }

}