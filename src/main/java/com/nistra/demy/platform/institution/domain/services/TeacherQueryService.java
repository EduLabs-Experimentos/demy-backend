package com.nistra.demy.platform.institution.domain.services;

import com.nistra.demy.platform.institution.domain.model.aggregates.Teacher;
import com.nistra.demy.platform.institution.domain.model.queries.*;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;

import java.util.List;
import java.util.Optional;

public interface TeacherQueryService {

    List<Teacher> handle(GetAllTeachersQuery query);

    Optional<Teacher> handle(GetTeacherByFullNameQuery query);

    Optional<Teacher> handle(GetTeacherByIdQuery query);

    Optional<Teacher> handle(GetCurrentTeacherQuery query);

    Optional<EmailAddress> handle(GetTeacherEmailAddressByUserIdQuery query);

    Optional<Teacher> handle(GetTeacherByUserIdQuery query);
}
