package com.nistra.demy.platform.scheduling.application.internal.commandservices;

import com.nistra.demy.platform.institution.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.scheduling.application.internal.outboundservices.acl.ExternalInstitutionService;
import com.nistra.demy.platform.scheduling.domain.exceptions.ScheduleTeacherNotFoundException;
import com.nistra.demy.platform.scheduling.domain.exceptions.WeeklyScheduleAlreadyExistsException;
import com.nistra.demy.platform.scheduling.domain.exceptions.WeeklyScheduleNotFoundException;
import com.nistra.demy.platform.scheduling.domain.model.aggregates.WeeklySchedule;
import com.nistra.demy.platform.scheduling.domain.model.commands.*;
import com.nistra.demy.platform.scheduling.domain.model.entities.Schedule;
import com.nistra.demy.platform.scheduling.domain.model.valueobjects.DayOfWeek;
import com.nistra.demy.platform.scheduling.domain.services.WeeklyScheduleCommandService;
import com.nistra.demy.platform.scheduling.infrastructure.persistence.jpa.repositories.ScheduleRepository;
import com.nistra.demy.platform.scheduling.infrastructure.persistence.jpa.repositories.WeeklyScheduleRepository;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Weekly Schedule Command Service Implementation
 * <p>This class implements the weekly schedule command service interface and provides the business logic for handling weekly schedule commands such as create, update, delete, and schedule management operations.</p>
 */
@Service
public class WeeklyScheduleCommandServiceImpl implements WeeklyScheduleCommandService {

    private final WeeklyScheduleRepository weeklyScheduleRepository;
    private final ExternalInstitutionService externalInstitutionService;
    private final ScheduleRepository scheduleRepository;
    private final ExternalIamService externalIamService;

    /**
     * Constructor that initializes the service with the required repositories and external services.
     * @param weeklyScheduleRepository The weekly schedule repository.
     * @param externalInstitutionService The external IAM service for teacher validation.
     * @param scheduleRepository The schedule repository.
     */
    public WeeklyScheduleCommandServiceImpl(WeeklyScheduleRepository weeklyScheduleRepository,
                                            ExternalInstitutionService externalInstitutionService,
                                            ScheduleRepository scheduleRepository,
                                            ExternalIamService externalIamService
                                            ) {
        this.weeklyScheduleRepository = weeklyScheduleRepository;
        this.externalInstitutionService = externalInstitutionService;
        this.scheduleRepository = scheduleRepository;
        this.externalIamService = externalIamService;
    }

    private AcademyId getContextAcademyId() {
        return externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalArgumentException("Academy context not found for the current user"));
    }

    /**
     * This method is used to handle the creation of a new weekly schedule.
     * @param command The create weekly schedule command containing the weekly schedule data.
     * @return The ID of the created weekly schedule.
     * @throws IllegalArgumentException if a weekly schedule with the same name already exists.
     * @see CreateWeeklyScheduleCommand
     * @see WeeklySchedule
     */
    @Override
    public Long handle(CreateWeeklyScheduleCommand command) {
        var academyId = getContextAcademyId();

        if (weeklyScheduleRepository.existsByNameAndAcademyId(command.name(), academyId)) {
            throw new WeeklyScheduleAlreadyExistsException(command.name());
        }

        var weeklySchedule = new WeeklySchedule(command, academyId);
        weeklyScheduleRepository.save(weeklySchedule);
        return weeklySchedule.getId();
    }

    /**
     * This method is used to handle the update of a weekly schedule name.
     * @param command The update weekly schedule name command containing the new name data.
     * @return An optional with the updated weekly schedule if successful.
     * @throws IllegalArgumentException if the weekly schedule is not found or if a weekly schedule with the same name already exists.
     * @see UpdateWeeklyScheduleNameCommand
     * @see WeeklySchedule
     */
    @Override
    public Optional<WeeklySchedule> handle(UpdateWeeklyScheduleNameCommand command) {
        var academyId = getContextAcademyId();

        var weeklyScheduleOpt = weeklyScheduleRepository.findById(command.weeklyScheduleId());
        if (weeklyScheduleOpt.isEmpty()) {
            throw new WeeklyScheduleNotFoundException(command.weeklyScheduleId());
        }

        var weeklySchedule = weeklyScheduleOpt.get();

        if (!weeklySchedule.getAcademyId().equals(academyId)) {
            throw new WeeklyScheduleNotFoundException(command.weeklyScheduleId());
        }

        if (weeklyScheduleRepository.existsByNameAndIdNotAndAcademyId(command.name(), command.weeklyScheduleId(), academyId)) {
            throw new WeeklyScheduleAlreadyExistsException(command.name());
        }

        weeklySchedule.updateName(command.name());
        weeklyScheduleRepository.save(weeklySchedule);
        return Optional.of(weeklySchedule);
    }

    /**
     * This method is used to handle adding a schedule to a weekly schedule.
     * @param command The add schedule to weekly command containing the schedule data.
     * @return An optional with the updated weekly schedule if successful.
     * @throws IllegalArgumentException if the weekly schedule is not found or if the teacher is not found.
     * @see AddScheduleToWeeklyCommand
     * @see WeeklySchedule
     * @see DayOfWeek
     */
    @Override
    public Optional<WeeklySchedule> handle(AddScheduleToWeeklyCommand command) {
        var academyId = getContextAcademyId();

        var weeklyScheduleOpt = weeklyScheduleRepository.findById(command.weeklyScheduleId());
        if (weeklyScheduleOpt.isEmpty()) {
            throw new WeeklyScheduleNotFoundException(command.weeklyScheduleId());
        }

        var weeklySchedule = weeklyScheduleOpt.get();

        if (!weeklySchedule.getAcademyId().equals(academyId)) {
            throw new WeeklyScheduleNotFoundException(command.weeklyScheduleId());
        }

        var dayOfWeek = DayOfWeek.valueOf(command.dayOfWeek().toUpperCase());

        UserId teacherId = externalInstitutionService
                .fetchTeacherIdByFullName(command.teacherFirstName(), command.teacherLastName())
                .orElseThrow(() -> new ScheduleTeacherNotFoundException(command.teacherFirstName(), command.teacherLastName()));

        weeklySchedule.addSchedule(
                command.startTime(),
                command.endTime(),
                dayOfWeek,
                command.courseId(),
                command.classroomId(),
                teacherId.userId()
        );

        weeklyScheduleRepository.save(weeklySchedule);
        return Optional.of(weeklySchedule);
    }

    /**
     * This method is used to handle removing a schedule from a weekly schedule.
     * @param command The remove schedule from weekly command containing the schedule ID to remove.
     * @return An optional with the updated weekly schedule if successful.
     * @throws IllegalArgumentException if the weekly schedule is not found.
     * @see RemoveScheduleFromWeeklyCommand
     * @see WeeklySchedule
     */
    @Override
    public Optional<WeeklySchedule> handle(RemoveScheduleFromWeeklyCommand command) {
        var academyId = getContextAcademyId();

        var weeklyScheduleOpt = weeklyScheduleRepository.findById(command.weeklyScheduleId());
        if (weeklyScheduleOpt.isEmpty()) {
            throw new WeeklyScheduleNotFoundException(command.weeklyScheduleId());
        }

        var weeklySchedule = weeklyScheduleOpt.get();

        if (!weeklySchedule.getAcademyId().equals(academyId)) {
            throw new WeeklyScheduleNotFoundException(command.weeklyScheduleId());
        }

        weeklySchedule.removeSchedule(command.scheduleId());
        weeklyScheduleRepository.save(weeklySchedule);
        return Optional.of(weeklySchedule);
    }

    /**
     * This method is used to handle the deletion of an existing weekly schedule.
     * @param command The delete weekly schedule command containing the weekly schedule ID to delete.
     * @throws IllegalArgumentException if the weekly schedule to delete is not found or if there's an error deleting the weekly schedule.
     * @see DeleteWeeklyScheduleCommand
     */
    @Override
    public void handle(DeleteWeeklyScheduleCommand command) {
        var academyId = getContextAcademyId();

        var weeklySchedule = weeklyScheduleRepository.findById(command.weeklyScheduleId())
                .orElseThrow(() -> new WeeklyScheduleNotFoundException(command.weeklyScheduleId()));

        if (!weeklySchedule.getAcademyId().equals(academyId)) {
            throw new IllegalArgumentException("Weekly schedule with id " + command.weeklyScheduleId() + " does not belong to the current academy");
        }

        try {
            weeklyScheduleRepository.deleteById(command.weeklyScheduleId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deleting weekly schedule: " + e.getMessage(), e);
        }
    }

    /**
     * This method is used to handle the update of an existing schedule.
     * @param command The update schedule command containing the updated schedule data.
     * @return An optional with the updated schedule if successful.
     * @throws IllegalArgumentException if the schedule to update is not found.
     * @see UpdateScheduleCommand
     * @see Schedule
     * @see DayOfWeek
     */
    @Override
    public Optional<Schedule> handle(UpdateScheduleCommand command) {
        var academyId = getContextAcademyId();

        var scheduleOpt = scheduleRepository.findById(command.scheduleId());
        if (scheduleOpt.isEmpty()) {
            throw new IllegalArgumentException("Schedule with id " + command.scheduleId() + " not found");
        }

        var schedule = scheduleOpt.get();

        if (!schedule.getWeeklySchedule().getAcademyId().equals(academyId)) {
            throw new IllegalArgumentException("Schedule with id " + command.scheduleId() + " does not belong to the current academy context.");
        }

        schedule.updateSchedule(
                command.classroomId(),
                command.startTime(),
                command.endTime(),
                DayOfWeek.valueOf(command.dayOfWeek().toUpperCase())
        );
        scheduleRepository.save(schedule);
        return Optional.of(schedule);
    }
}
