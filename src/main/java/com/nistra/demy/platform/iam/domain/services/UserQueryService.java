package com.nistra.demy.platform.iam.domain.services;

import com.nistra.demy.platform.iam.domain.model.aggregates.User;
import com.nistra.demy.platform.iam.domain.model.queries.GetAuthenticatedUserIdQuery;
import com.nistra.demy.platform.iam.domain.model.queries.GetAuthenticatedUserTenantIdQuery;
import com.nistra.demy.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.nistra.demy.platform.iam.domain.model.valueobjects.TenantId;

import java.util.Optional;

public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery query);

    Optional<Long> handle(GetAuthenticatedUserIdQuery query);

    Optional<TenantId> handle(GetAuthenticatedUserTenantIdQuery query);
}
