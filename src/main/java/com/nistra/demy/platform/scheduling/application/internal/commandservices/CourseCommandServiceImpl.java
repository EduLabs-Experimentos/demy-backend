package com.nistra.demy.platform.scheduling.application.internal.commandservices;

import com.nistra.demy.platform.institution.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.scheduling.domain.exceptions.CourseAlreadyExistsException;
import com.nistra.demy.platform.scheduling.domain.exceptions.CourseNotFoundException;
import com.nistra.demy.platform.scheduling.domain.model.aggregates.Course;
import com.nistra.demy.platform.scheduling.domain.model.commands.CreateCourseCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.DeleteCourseCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.UpdateCourseCommand;
import com.nistra.demy.platform.scheduling.domain.services.CourseCommandService;
import com.nistra.demy.platform.scheduling.infrastructure.persistence.jpa.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Course Command Service Implementation
 * <p>This class implements the course command service interface and provides the business logic for handling course commands such as create, update, and delete operations.</p>
 */
@Service
public class CourseCommandServiceImpl implements CourseCommandService {

    private final CourseRepository courseRepository;
    private final ExternalIamService externalIamService;

    /**
     * Constructor that initializes the service with the required repository.
     * @param courseRepository The course repository.
     */
    public CourseCommandServiceImpl(CourseRepository courseRepository, ExternalIamService externalIamService) {
        this.courseRepository = courseRepository;
        this.externalIamService = externalIamService;
    }

    /**
     * This method is used to handle the creation of a new course.
     * @param command The create course command containing the course data.
     * @return The ID of the created course.
     * @throws IllegalArgumentException if a course with the same name already exists or if there's an error saving the course.
     * @see CreateCourseCommand
     * @see Course
     */
    @Override
    public Long handle(CreateCourseCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalArgumentException("Academy context not found for the current user"));

        if (courseRepository.existsByNameAndAcademyId(command.name(),academyId)) {
            throw new CourseAlreadyExistsException(command.name());
        }

        var course = new Course(command,academyId);
        try {
            courseRepository.save(course);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving course: " + e.getMessage(), e);
        }
        return course.getId();
    }

    /**
     * This method is used to handle the update of an existing course.
     * @param command The update course command containing the updated course data.
     * @return An optional with the updated course if successful, otherwise an empty optional.
     * @throws IllegalArgumentException if a course with the same name already exists, if the course to update is not found, or if there's an error updating the course.
     * @see UpdateCourseCommand
     * @see Course
     */
    @Override
    public Optional<Course> handle(UpdateCourseCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalArgumentException("Academy context not found for the current user"));

        if (courseRepository.existsByNameAndIdNotAndAcademyId(command.name(), command.courseId(), academyId)) {
            throw new CourseAlreadyExistsException(command.name());
        }

        var result = courseRepository.findById(command.courseId());
        if (result.isEmpty()) {
            throw new CourseNotFoundException(command.courseId());
        }

        var courseToUpdate = result.get();

        if (!courseToUpdate.getAcademyId().equals(academyId)) {
            throw new CourseNotFoundException(command.courseId());
        }
        try {
            var updatedCourse = courseRepository.save(courseToUpdate.updateCourse(command));
            return Optional.of(updatedCourse);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating course: " + e.getMessage());
        }

    }

    /**
     * This method is used to handle the deletion of an existing course.
     * @param command The delete course command containing the course ID to delete.
     * @throws IllegalArgumentException if the course to delete is not found or if there's an error deleting the course.
     * @see DeleteCourseCommand
     */
    @Override
    public void handle(DeleteCourseCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalArgumentException("Academy context not found for the current user"));

        var course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new CourseNotFoundException(command.courseId()));

        if (!course.getAcademyId().equals(academyId)) {
            throw new CourseNotFoundException(command.courseId());
        }

        try {
            courseRepository.deleteById(command.courseId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deleting course: " + e.getMessage(), e);
        }

    }
}
