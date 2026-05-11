package com.nistra.demy.platform.enrollment.application.internal.commandservices;

import com.nistra.demy.platform.enrollment.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.enrollment.application.internal.outboundservices.acl.ExternalSchedulingService;
import com.nistra.demy.platform.enrollment.domain.exceptions.EnrollmentAlreadyExistsException;
import com.nistra.demy.platform.enrollment.domain.exceptions.EnrollmentNotFoundException;
import com.nistra.demy.platform.enrollment.domain.model.aggregates.Enrollment;
import com.nistra.demy.platform.enrollment.domain.model.commands.CreateEnrollmentCommand;
import com.nistra.demy.platform.enrollment.domain.model.commands.DeleteEnrollmentCommand;
import com.nistra.demy.platform.enrollment.domain.model.commands.UpdateEnrollmentCommand;
import com.nistra.demy.platform.enrollment.domain.services.EnrollmentCommandService;
import com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories.EnrollmentRepository;
import com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnrollmentCommandServiceImpl implements EnrollmentCommandService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ExternalSchedulingService externalSchedulingService;
    private final ExternalIamService externalIamService;

    public EnrollmentCommandServiceImpl(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            ExternalSchedulingService externalSchedulingService,
            ExternalIamService externalIamService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.externalSchedulingService = externalSchedulingService;
        this.externalIamService = externalIamService;
    }

    @Override
    public Long handle(CreateEnrollmentCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalArgumentException("Academy not found"));

        var scheduleId = externalSchedulingService.fetchScheduleById(command.scheduleId().scheduleId())
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        var existing = enrollmentRepository.findByStudentIdAndPeriodId(
                command.studentId(),
                command.periodId()
        );

        if (existing.isPresent()) {
            throw new EnrollmentAlreadyExistsException(command.studentId(), command.periodId());
        }

        var enrollment = Enrollment.createEnrollmentActive(
                command.studentId(),
                command.periodId(),
                scheduleId,
                academyId,
                command.money(),
                command.paymentStatus()
        );

        var student = studentRepository.findById(command.studentId().studentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        enrollment.registerEnrollmentEvent(student.getDni());

        try {
            enrollmentRepository.save(enrollment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create enrollment"+ e.getMessage(), e);
        }
        return enrollment.getId();
    }

    @Override
    public void handle(DeleteEnrollmentCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalArgumentException("Academy context not found"));

        var enrollment = enrollmentRepository.findById(command.enrollmentId())
                .orElseThrow(() -> new EnrollmentNotFoundException(command.enrollmentId()));

        if (!enrollment.getAcademyId().equals(academyId)) {
            throw new EnrollmentNotFoundException(command.enrollmentId());
        }

        try {
            enrollmentRepository.deleteById(command.enrollmentId());
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting enrollment: " + e.getMessage(), e);

        }
    }

    @Override
    public Optional<Enrollment> handle(UpdateEnrollmentCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalArgumentException("Academy context not found"));

        var enrollmentToUpdate = enrollmentRepository.findById(command.enrollmentId())
                .orElseThrow(() -> new EnrollmentNotFoundException(command.enrollmentId()));

        if (!enrollmentToUpdate.getAcademyId().equals(academyId)) {
            throw new EnrollmentNotFoundException(command.enrollmentId());
        }

        try {
            enrollmentToUpdate.updateInformation(
                    command.money(),
                    command.enrollmentStatus(),
                    command.paymentStatus()
            );
            var updatedEnrollment = enrollmentRepository.save(enrollmentToUpdate);
            return Optional.of(updatedEnrollment);
        } catch (Exception e) {
            throw new RuntimeException("Error while updating enrollment: " + e.getMessage(), e);

        }
    }
}
