package com.nistra.demy.platform.iam.application.internal.queryservices;

import com.nistra.demy.platform.iam.application.internal.outboundservices.identity.IdentityService;
import com.nistra.demy.platform.iam.domain.model.aggregates.User;
import com.nistra.demy.platform.iam.domain.model.queries.GetAuthenticatedUserIdQuery;
import com.nistra.demy.platform.iam.domain.model.queries.GetAuthenticatedUserTenantIdQuery;
import com.nistra.demy.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.nistra.demy.platform.iam.domain.model.valueobjects.TenantId;
import com.nistra.demy.platform.iam.domain.services.UserQueryService;
import com.nistra.demy.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;
    private final IdentityService identityService;

    public UserQueryServiceImpl(UserRepository userRepository, IdentityService identityService) {
        this.userRepository = userRepository;
        this.identityService = identityService;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<Long> handle(GetAuthenticatedUserIdQuery query) {
        return identityService.getUserId();
    }

    @Override
    public Optional<TenantId> handle(GetAuthenticatedUserTenantIdQuery query) {
        return Optional.ofNullable(identityService.getTenantId()
                .map(TenantId::new)
                .orElseThrow(() -> new IllegalStateException("Tenant ID is required but not present")));
    }
}
