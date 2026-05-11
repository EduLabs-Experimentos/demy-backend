package com.nistra.demy.platform.enrollment.domain.services;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.Student;
import com.nistra.demy.platform.enrollment.domain.model.commands.CreateStudentCommand;
import com.nistra.demy.platform.enrollment.domain.model.commands.DeleteStudentCommand;
import com.nistra.demy.platform.enrollment.domain.model.commands.UpdateStudentCommand;

import java.util.Optional;

public interface StudentCommandService {
    Long handle(CreateStudentCommand command);
    void handle(DeleteStudentCommand command);
    Optional<Student> handle(UpdateStudentCommand command);
}
