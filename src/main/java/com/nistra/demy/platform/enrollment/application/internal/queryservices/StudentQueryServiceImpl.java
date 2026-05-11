package com.nistra.demy.platform.enrollment.application.internal.queryservices;

import com.nistra.demy.platform.enrollment.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.enrollment.domain.model.aggregates.Student;
import com.nistra.demy.platform.enrollment.domain.model.queries.*;
import com.nistra.demy.platform.enrollment.domain.services.StudentQueryService;
import com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories.StudentRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StudentId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentQueryServiceImpl implements StudentQueryService {

    private final StudentRepository studentRepository;
    private final ExternalIamService externalIamService;

    public StudentQueryServiceImpl(
            StudentRepository studentRepository,
            ExternalIamService externalIamService
    ) {
        this.studentRepository = studentRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public Optional<Student> handle(GetStudentByIdQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("Current academy ID not found"));

        return studentRepository.findById(query.studentId())
                .filter(student -> student.getAcademyId().equals(academyId));
    }

    @Override
    public List<Student> handle(GetAllStudentsQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("Current academy ID not found"));

        return studentRepository.findAllByAcademyId(academyId);
    }

    @Override
    public Optional<Student> handle(GetStudentByDniQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("Current academy ID not found"));

        return studentRepository.findByDni(query.dniNumber())
                .filter(student -> student.getAcademyId().equals(academyId));
    }

    @Override
    public Optional<EmailAddress> handle(GetStudentEmailAddressByUserIdQuery query) {
        return externalIamService.fetchStudentEmailByUserId(query.userId().userId());
    }

    @Override
    public Optional<StudentId> handle(GetStudentIdByDniQuery query) {
        var studentId = studentRepository.findByDni(query.dniNumber())
                .orElseThrow(() -> new RuntimeException("Student not found for DNI: %s".formatted(query.dniNumber().dniNumber())));
        return Optional.of(new StudentId(studentId.getId()));
    }

    @Override
    public Optional<Student> handle(GetStudentByUserIdQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("Current academy ID not found"));

        return studentRepository.findByUserId(query.userId())
                .filter(student -> student.getAcademyId().equals(academyId));
    }

    @Override
    public Optional<Student> handle(GetCurrentStudentQuery query) {
        var currentUserId = externalIamService.fetchCurrentUserId()
                .orElseThrow(() -> new IllegalStateException("Current user ID not found"));
        return studentRepository.findByUserId(currentUserId);
    }
}
