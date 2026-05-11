package com.nistra.demy.platform.scheduling.infrastructure.persistence.jpa.repositories;

import com.nistra.demy.platform.scheduling.domain.model.entities.Schedule;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Schedule} entities.
 * Provides methods for performing CRUD operations and additional queries related to schedules.
 *
 * Extends {@link JpaRepository} to inherit standard CRUD operations.
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    /**
     * Finds a list of schedules assigned to a specific teacher.
     *
     * @param teacherId The unique identifier of the teacher.
     * @return A list of {@link Schedule} entities associated with the specified teacher.
     */
    List<Schedule> findByTeacherId(UserId teacherId);
}
