package com.nistra.demy.platform.attendance.application.internal.querysevices;

import com.nistra.demy.platform.attendance.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.attendance.domain.exceptions.AcademyIdNotFoundException;
import com.nistra.demy.platform.attendance.domain.model.aggregates.ClassAttendance;
import com.nistra.demy.platform.attendance.domain.model.queries.GetAllClassAttendancesByAcademyQuery;
import com.nistra.demy.platform.attendance.domain.model.queries.GetClassAttendanceByIdQuery;
import com.nistra.demy.platform.attendance.domain.services.ClassAttendanceQueryService;
import com.nistra.demy.platform.attendance.infrastructure.persistence.jpa.repositories.ClassAttendanceRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ClassAttendanceQueryService Implementation
 *
 * @summary
 * Implementation of the ClassAttendanceQueryService interface.
 * It is responsible for handling favorite source queries.
 *
 * @since 1.0
 */

@Service
public class ClassAttendanceQueryServiceImpl implements ClassAttendanceQueryService {

    private final ClassAttendanceRepository repository;
    private final ExternalIamService externalIamService;

    public ClassAttendanceQueryServiceImpl(ClassAttendanceRepository repository,
                                           ExternalIamService externalIamService) {
        this.repository = repository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassAttendance> handle(GetAllClassAttendancesByAcademyQuery query) {
        AcademyId academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(AcademyIdNotFoundException::new);;
        return repository.findAllByAcademyId(academyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClassAttendance> handle(GetClassAttendanceByIdQuery query) {
        AcademyId academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(AcademyIdNotFoundException::new);;
        return repository.findByIdAndAcademyId(query.id(), academyId);
    }

}
