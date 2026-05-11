package com.nistra.demy.platform.iam.domain.model.queries;

public record GetUserByIdQuery(Long userId) {
    public GetUserByIdQuery {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("UserId cannot be null or less than or equal to zero");
    }
}
