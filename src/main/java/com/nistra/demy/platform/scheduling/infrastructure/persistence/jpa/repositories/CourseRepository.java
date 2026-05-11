package com.nistra.demy.platform.scheduling.infrastructure.persistence.jpa.repositories;

import com.nistra.demy.platform.scheduling.domain.model.aggregates.Course;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Course} entities.
 * Provides methods for performing CRUD operations and additional queries related to courses.
 * Extends {@link JpaRepository} to inherit standard CRUD operations.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Checks if a course with the specified name already exists.
     *
     * @param name The name of the course to check.
     * @return {@code true} if a course with the given name exists, otherwise {@code false}.
     */
    boolean existsByNameAndAcademyId(String name, AcademyId academyId);

    /**
     * Checks if a course with the specified name exists, excluding a specific course by its ID.
     *
     * @param name The name of the course to check.
     * @param id The ID of the course to exclude from the check.
     * @return {@code true} if a course with the given name exists and its ID is not the specified one, otherwise {@code false}.
     */
    boolean existsByNameAndIdNotAndAcademyId(String name, Long id, AcademyId academyId);


    List<Course> findAllByAcademyId(AcademyId academyId);
}
