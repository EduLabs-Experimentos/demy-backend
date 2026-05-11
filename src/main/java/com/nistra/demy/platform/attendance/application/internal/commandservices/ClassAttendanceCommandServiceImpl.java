package com.nistra.demy.platform.attendance.application.internal.commandservices;

import com.nistra.demy.platform.attendance.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.attendance.domain.exceptions.AcademyIdNotFoundException;
import com.nistra.demy.platform.attendance.domain.exceptions.AttendanceNotFoundException;
import com.nistra.demy.platform.attendance.domain.exceptions.ClassAttendanceAlreadyExistsException;
import com.nistra.demy.platform.attendance.domain.model.aggregates.ClassAttendance;
import com.nistra.demy.platform.attendance.domain.model.commands.CreateClassAttendanceCommand;
import com.nistra.demy.platform.attendance.domain.model.commands.DeleteClassAttendanceCommand;
import com.nistra.demy.platform.attendance.domain.model.commands.UpdateAttendanceRecordStatusCommand;
import com.nistra.demy.platform.attendance.domain.services.ClassAttendanceCommandService;
import com.nistra.demy.platform.attendance.infrastructure.persistence.jpa.repositories.ClassAttendanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * ClassAttendanceCommandService Implementation
 *
 * @summary
 * Implementation of the ClassAttendanceCommandService interface
 * It is responsible for handling class attendance commands.
 */
@Service
public class ClassAttendanceCommandServiceImpl  implements ClassAttendanceCommandService {
    private final ClassAttendanceRepository classAttendanceRepository;
    private final ExternalIamService externalIamService;

    public ClassAttendanceCommandServiceImpl(
            ExternalIamService externalIamService,
            ClassAttendanceRepository classAttendanceRepository) {
        this.externalIamService = externalIamService;
        this.classAttendanceRepository = classAttendanceRepository;

    }


    @Override
    @Transactional
    public Optional<ClassAttendance> handle(CreateClassAttendanceCommand command) {

        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(AcademyIdNotFoundException::new);


        boolean exists = classAttendanceRepository
                .existsByAcademyIdAndClassSessionIdAndDate(
                        academyId, command.classSessionId(), command.date()
                );
        if (exists) {
            throw new ClassAttendanceAlreadyExistsException(command.classSessionId(),command.date());
        }


        var aggregate = new ClassAttendance(academyId, command);


        var created = classAttendanceRepository.save(aggregate);
        return Optional.of(created);
    }

    @Transactional
    public Optional<ClassAttendance> handle(UpdateAttendanceRecordStatusCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(AcademyIdNotFoundException::new);

        var aggregate = classAttendanceRepository
                .findByIdAndAcademyId(command.classAttendanceId(), academyId)
                .orElseThrow(AttendanceNotFoundException::new);

        aggregate.updateRecordStatus(command.dni(), command.newStatus());

        return Optional.of(aggregate);
    }

    @Override
    @Transactional
    public boolean handle(DeleteClassAttendanceCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(AcademyIdNotFoundException::new);

        var maybe = classAttendanceRepository.findByIdAndAcademyId(command.id(), academyId);
        if (maybe.isEmpty()) return false;

        classAttendanceRepository.delete(maybe.get());
        return true;
    }
}
