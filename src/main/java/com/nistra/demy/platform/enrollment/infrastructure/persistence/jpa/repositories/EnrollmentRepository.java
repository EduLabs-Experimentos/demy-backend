package com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.Enrollment;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.PeriodId;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.StudentId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findAllByAcademyId(AcademyId academyId);
    List<Enrollment> findAllByStudentIdAndAcademyId(StudentId studentId, AcademyId academyId);
    Optional<Enrollment> findByStudentIdAndPeriodId(StudentId studentId, PeriodId periodId);
}
