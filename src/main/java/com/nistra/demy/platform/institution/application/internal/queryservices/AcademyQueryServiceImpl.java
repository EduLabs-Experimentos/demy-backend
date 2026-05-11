package com.nistra.demy.platform.institution.application.internal.queryservices;

import com.nistra.demy.platform.institution.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.institution.domain.model.aggregates.Academy;
import com.nistra.demy.platform.institution.domain.model.queries.ExistsAcademyByIdQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetAcademyByIdQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetCurrentAcademyQuery;
import com.nistra.demy.platform.institution.domain.services.AcademyQueryService;
import com.nistra.demy.platform.institution.infrastructure.persistence.jpa.repositories.AcademyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AcademyQueryServiceImpl implements AcademyQueryService {

    private final AcademyRepository academyRepository;
    private final ExternalIamService externalIamService;

    public AcademyQueryServiceImpl(
            AcademyRepository academyRepository,
            ExternalIamService externalIamService
    ) {
        this.academyRepository = academyRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public Optional<Academy> handle(GetAcademyByIdQuery query) {
        return academyRepository.findById(query.academyId());
    }

    @Override
    public boolean handle(ExistsAcademyByIdQuery query) {
        return academyRepository.existsById(query.academyId());
    }

    @Override
    public Optional<Academy> handle(GetCurrentAcademyQuery query) {
        var currentAcademyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new RuntimeException("Academy fetch authenticated failed"));
        return academyRepository.findById(currentAcademyId.academyId());
    }
}
