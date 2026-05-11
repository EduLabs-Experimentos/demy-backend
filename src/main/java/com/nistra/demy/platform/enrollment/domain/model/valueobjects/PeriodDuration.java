package com.nistra.demy.platform.enrollment.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

/**
 * PeriodDuration Value Object
 */
@Embeddable
public record PeriodDuration(LocalDate startDate, LocalDate endDate) {
    /**
     * Default constructor
     */
    public PeriodDuration() {
        this(null, null);
    }

    /**
     * Constructor with validation
     * @param startDate The start date of the period
     * @param endDate The end date of the period
     */
    public PeriodDuration {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date must not be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date must not be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
    }

    /**
     * Check if the period is currently active based on current date
     * @return true if the current date is within the period duration
     */
    public boolean isCurrentlyActive() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    /**
     * Get the duration in days
     * @return the number of days between start and end date
     */
    public long getDurationInDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }
}