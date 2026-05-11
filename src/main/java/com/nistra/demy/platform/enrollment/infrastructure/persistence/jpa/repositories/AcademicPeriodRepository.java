package com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.AcademicPeriod;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcademicPeriodRepository extends JpaRepository<AcademicPeriod, Long> {
    boolean existsByPeriodName (String periodName);
    boolean existsByPeriodNameAndIdIsNot(String periodName, Long id);
    List<AcademicPeriod> findAllByAcademyId(AcademyId academyId);
}
