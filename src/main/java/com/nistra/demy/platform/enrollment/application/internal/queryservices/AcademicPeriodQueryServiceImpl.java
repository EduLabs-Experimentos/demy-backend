package com.nistra.demy.platform.enrollment.application.internal.queryservices;

import com.nistra.demy.platform.enrollment.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.enrollment.domain.model.aggregates.AcademicPeriod;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAcademicPeriodByIdQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAllAcademicPeriodsQuery;
import com.nistra.demy.platform.enrollment.domain.services.AcademicPeriodQueryService;
import com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories.AcademicPeriodRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcademicPeriodQueryServiceImpl implements AcademicPeriodQueryService {
    private final AcademicPeriodRepository academicPeriodRepository;
    private final ExternalIamService externalIamService;

    public AcademicPeriodQueryServiceImpl(
            AcademicPeriodRepository academicPeriodRepository,
            ExternalIamService externalIamService) {
        this.academicPeriodRepository = academicPeriodRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public List<AcademicPeriod> handle(GetAllAcademicPeriodsQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("Current academy ID not found"));
        return academicPeriodRepository.findAllByAcademyId(academyId);
    }

    @Override
    public Optional<AcademicPeriod> handle(GetAcademicPeriodByIdQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("Current academy ID not found"));
        return academicPeriodRepository.findById(query.academicPeriodId())
                .filter(academicPeriod -> academicPeriod.getAcademyId().equals(academyId));
    }
}
