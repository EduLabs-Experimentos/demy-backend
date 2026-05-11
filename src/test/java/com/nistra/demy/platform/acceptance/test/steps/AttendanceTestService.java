package com.nistra.demy.platform.acceptance.test.steps;

import com.nistra.demy.platform.attendance.domain.model.aggregates.ClassAttendance;
import com.nistra.demy.platform.attendance.domain.model.commands.CreateClassAttendanceCommand;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceStatus;
import com.nistra.demy.platform.attendance.infrastructure.persistence.jpa.repositories.ClassAttendanceRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttendanceTestService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ClassAttendanceRepository classAttendanceRepository;

    public AttendanceTestService(ClassAttendanceRepository classAttendanceRepository) {
        this.classAttendanceRepository = classAttendanceRepository;
    }

    @Transactional
    public ClassAttendance createAttendance(AcademyId academyId, CreateClassAttendanceCommand cmd) {
        ClassAttendance attendance = new ClassAttendance(academyId, cmd);
        classAttendanceRepository.save(attendance);
        entityManager.flush();
        return reloadWithRecords(attendance.getId(), academyId);
    }

    @Transactional
    public ClassAttendance updateStatus(Long id, AcademyId academyId,
                                        DniNumber dni, AttendanceStatus status) {
        ClassAttendance attendance = reloadWithRecords(id, academyId);
        attendance.updateRecordStatus(dni, status);
        classAttendanceRepository.save(attendance);
        entityManager.flush();
        entityManager.clear();
        return reloadWithRecords(id, academyId);
    }

    @Transactional(readOnly = true)
    public ClassAttendance reloadWithRecords(Long id, AcademyId academyId) {
        return entityManager.createQuery(
                        "SELECT ca FROM ClassAttendance ca " +
                                "LEFT JOIN FETCH ca.attendance " +
                                "WHERE ca.id = :id AND ca.academyId = :academyId",
                        ClassAttendance.class)
                .setParameter("id", id)
                .setParameter("academyId", academyId)
                .getSingleResult();
    }

    @Transactional
    public void deleteAll() {
        classAttendanceRepository.deleteAll();
    }
}