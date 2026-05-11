package com.nistra.demy.platform.enrollment.application.internal.queryservices;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.Enrollment;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAllEnrollmentsByStudentDniQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAllEnrollmentsByStudentIdQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAllEnrollmentsQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetEnrollmentByIdQuery;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.StudentId;
import com.nistra.demy.platform.enrollment.domain.services.EnrollmentQueryService;
import com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories.EnrollmentRepository;
import com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories.StudentRepository;
import com.nistra.demy.platform.institution.application.internal.outboundservices.acl.ExternalIamService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentQueryServiceImpl implements EnrollmentQueryService {

    private final EnrollmentRepository enrollmentRepository;
    private final ExternalIamService externalIamService;
    private final StudentRepository studentRepository;

    public EnrollmentQueryServiceImpl(
        EnrollmentRepository enrollmentRepository,
        ExternalIamService externalIamService,
        StudentRepository studentRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.externalIamService = externalIamService;
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Enrollment> handle(GetAllEnrollmentsByStudentIdQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("Current academy ID not found"));

        return enrollmentRepository.findAllByStudentIdAndAcademyId(query.studentId(), academyId);
    }

    @Override
    public List<Enrollment> handle(GetAllEnrollmentsQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("Current academy ID not found"));
        return enrollmentRepository.findAllByAcademyId(academyId);
    }

    @Override
    public Optional<Enrollment> handle(GetEnrollmentByIdQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("Current academy ID not found"));

        return enrollmentRepository.findById(query.enrollmentId())
                .filter(enrollment -> enrollment.getAcademyId().equals(academyId));
    }

    @Override
    public List<Enrollment> handle(GetAllEnrollmentsByStudentDniQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("Current academy ID not found"));

        var student = studentRepository.findByDni(query.dni())
                .filter(  s -> s.getAcademyId().equals(academyId));

        if (student.isEmpty()) {
            throw new IllegalArgumentException("Student with DNI %s not found".formatted(query.dni()));
        }
        var studentId = new StudentId(student.get().getId());
        return enrollmentRepository.findAllByStudentIdAndAcademyId(studentId, academyId);
    }
}
