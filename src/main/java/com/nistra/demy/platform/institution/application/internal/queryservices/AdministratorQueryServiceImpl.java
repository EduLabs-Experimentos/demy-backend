package com.nistra.demy.platform.institution.application.internal.queryservices;

import com.nistra.demy.platform.institution.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.institution.domain.model.aggregates.Administrator;
import com.nistra.demy.platform.institution.domain.model.queries.GetAdministratorByDniNumberQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetAdministratorEmailAddressByUserIdQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetCurrentAdministratorQuery;
import com.nistra.demy.platform.institution.domain.services.AdministratorQueryService;
import com.nistra.demy.platform.institution.infrastructure.persistence.jpa.repositories.AdministratorRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdministratorQueryServiceImpl implements AdministratorQueryService {

    private final AdministratorRepository administratorRepository;
    private final ExternalIamService externalIamService;

    public AdministratorQueryServiceImpl(
            AdministratorRepository administratorRepository,
            ExternalIamService externalIamService
    ) {
        this.administratorRepository = administratorRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public Optional<Administrator> handle(GetAdministratorByDniNumberQuery query) {
        return administratorRepository.findByDniNumber(query.dniNumber());
    }

    @Override
    public Optional<Administrator> handle(GetCurrentAdministratorQuery query) {
        var currentUserId = externalIamService.fetchCurrentUserId()
                .orElseThrow(() -> new RuntimeException("User fetch authenticated failed"));
        return administratorRepository.findByUserId(currentUserId);
    }

    @Override
    public Optional<EmailAddress> handle(GetAdministratorEmailAddressByUserIdQuery query) {
        return externalIamService.fetchEmailAddressByUserId(query.userId());
    }
}
