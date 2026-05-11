package com.nistra.demy.platform.institution.application.internal.commandservices;

import com.nistra.demy.platform.institution.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.institution.domain.model.aggregates.Teacher;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterTeacherCommand;
import com.nistra.demy.platform.institution.domain.services.TeacherCommandService;
import com.nistra.demy.platform.institution.infrastructure.persistence.jpa.repositories.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TeacherCommandServiceImpl implements TeacherCommandService {

    private final TeacherRepository teacherRepository;
    private final ExternalIamService externalIamService;

    public TeacherCommandServiceImpl(TeacherRepository teacherRepository, ExternalIamService externalIamService) {
        this.teacherRepository = teacherRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public Optional<Teacher> handle(RegisterTeacherCommand command) {
        try {
            var userId = externalIamService.registerTeacher(command.emailAddress().email())
                    .orElseThrow(() -> new RuntimeException("Failed to register teacher in IAM"));
            var academyId = externalIamService.fetchCurrentAcademyId()
                    .orElseThrow(() -> new RuntimeException("No academy found"));
            var teacher = new Teacher(command, userId, academyId);
            teacherRepository.save(teacher);
            return Optional.of(teacher);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register teacher: %s".formatted(e.getMessage()));
        }
    }
}
