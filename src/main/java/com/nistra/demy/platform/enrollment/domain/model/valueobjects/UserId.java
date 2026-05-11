package com.nistra.demy.platform.enrollment.domain.model.valueobjects;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(
        @Column(nullable = false)
        Long userId
) {
    public UserId() {
        this(0L);
    }

    public UserId {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID cannot be null or less than or equal to zero");
        }
    }
}