package com.nistra.demy.platform.institution.domain.services;

import com.nistra.demy.platform.institution.domain.model.aggregates.Teacher;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterTeacherCommand;

import java.util.Optional;

public interface TeacherCommandService {
    Optional<Teacher> handle(RegisterTeacherCommand command);
}
