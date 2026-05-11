package com.nistra.demy.platform.institution.application.internal.queryservices;

import com.nistra.demy.platform.institution.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.institution.domain.model.aggregates.Teacher;
import com.nistra.demy.platform.institution.domain.model.queries.*;
import com.nistra.demy.platform.institution.domain.services.TeacherQueryService;
import com.nistra.demy.platform.institution.infrastructure.persistence.jpa.repositories.TeacherRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherQueryServiceImpl implements TeacherQueryService {

    private final TeacherRepository teacherRepository;
    private final ExternalIamService externalIamService;

    public TeacherQueryServiceImpl(TeacherRepository teacherRepository, ExternalIamService externalIamService) {
        this.teacherRepository = teacherRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public List<Teacher> handle(GetAllTeachersQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("No academy context found for the current user"));
        return teacherRepository.findAllByAcademyId(academyId);
    }

    @Override
    public Optional<Teacher> handle(GetTeacherByIdQuery query) {
        return teacherRepository.findById(query.teacherId());
    }

    @Override
    public Optional<Teacher> handle(GetTeacherByFullNameQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("No academy context found for the current user"));
        return teacherRepository.findByPersonNameAndAcademyId(query.personName(),academyId);
    }

    @Override
    public Optional<Teacher> handle(GetCurrentTeacherQuery query) {
        var currentUserId = externalIamService.fetchCurrentUserId()
                .orElseThrow(() -> new RuntimeException("User fetch authenticated failed"));
        return teacherRepository.findByUserId(currentUserId);
    }

    @Override
    public Optional<EmailAddress> handle(GetTeacherEmailAddressByUserIdQuery query) {
        return externalIamService.fetchEmailAddressByUserId(query.userId());
    }

    @Override
    public Optional<Teacher> handle(GetTeacherByUserIdQuery query) {
        return teacherRepository.findByUserId(query.userId());
    }
}
