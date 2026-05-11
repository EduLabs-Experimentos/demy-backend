package com.nistra.demy.platform.enrollment.domain.services;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.Student;
import com.nistra.demy.platform.enrollment.domain.model.queries.*;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StudentId;

import java.util.List;
import java.util.Optional;

public interface StudentQueryService {
    Optional<Student> handle(GetStudentByIdQuery query);
    List<Student> handle(GetAllStudentsQuery query);
    Optional<Student> handle(GetStudentByDniQuery query);
    Optional<EmailAddress> handle(GetStudentEmailAddressByUserIdQuery query);
    Optional<StudentId> handle(GetStudentIdByDniQuery query);
    Optional<Student> handle(GetStudentByUserIdQuery query);
    Optional<Student> handle(GetCurrentStudentQuery query);
}
