package com.nistra.demy.platform.attendance.infrastructure.persistence.jpa.repositories;

import com.nistra.demy.platform.attendance.domain.model.aggregates.ClassAttendance;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.ClassSessionId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * JPA repository for ClassAttendance entity
 * @summary
 * This interface extends JpaRepository to provide CRUD operations for
 * ClassAttendance entity
 * It extends Spring Data JpaRepository with ClassAttendance as the entity
 * type and Long as the ID type
 */
public interface ClassAttendanceRepository extends JpaRepository<ClassAttendance, Long>{

    boolean existsByAcademyIdAndClassSessionIdAndDate(
            AcademyId academyId,
            ClassSessionId classSessionId,
            LocalDate date
    );

    Optional<ClassAttendance> findByIdAndAcademyId(Long id, AcademyId academyId);

    @EntityGraph(attributePaths = {"attendance"})
    List<ClassAttendance> findAllByAcademyId(AcademyId academyId);


}
