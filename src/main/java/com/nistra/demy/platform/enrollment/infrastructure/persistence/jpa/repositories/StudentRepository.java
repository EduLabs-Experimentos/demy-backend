package com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.Student;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsStudentByDni(DniNumber dniNumber);
    Optional<Student> findByDni(DniNumber dniNumber);
    List<Student> findAllByAcademyId(AcademyId academyId);
    boolean existsByDniAndIdNotAndAcademyId(DniNumber dni, Long id, AcademyId academyId);
    Optional<Student> findByUserId(UserId userId);
}
