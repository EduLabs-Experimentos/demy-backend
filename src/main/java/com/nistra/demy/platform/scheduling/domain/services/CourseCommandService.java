package com.nistra.demy.platform.scheduling.domain.services;

import com.nistra.demy.platform.scheduling.domain.model.aggregates.Course;
import com.nistra.demy.platform.scheduling.domain.model.commands.CreateCourseCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.DeleteCourseCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.UpdateCourseCommand;

import java.util.Optional;

public interface CourseCommandService {

    Long handle(CreateCourseCommand command);

    Optional<Course> handle(UpdateCourseCommand command);

    void handle(DeleteCourseCommand command);
}