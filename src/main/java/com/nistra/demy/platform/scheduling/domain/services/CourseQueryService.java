package com.nistra.demy.platform.scheduling.domain.services;

import com.nistra.demy.platform.scheduling.domain.model.aggregates.Course;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetAllCoursesQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetCourseByIdQuery;

import java.util.List;
import java.util.Optional;

public interface CourseQueryService {

    List<Course> handle(GetAllCoursesQuery query);

    Optional<Course> handle(GetCourseByIdQuery query);
}