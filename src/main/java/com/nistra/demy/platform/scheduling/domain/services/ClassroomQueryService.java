package com.nistra.demy.platform.scheduling.domain.services;

import com.nistra.demy.platform.scheduling.domain.model.aggregates.Classroom;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetAllClassroomsQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetClassroomByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ClassroomQueryService {

    List<Classroom> handle(GetAllClassroomsQuery query);

    Optional<Classroom> handle(GetClassroomByIdQuery query);
}