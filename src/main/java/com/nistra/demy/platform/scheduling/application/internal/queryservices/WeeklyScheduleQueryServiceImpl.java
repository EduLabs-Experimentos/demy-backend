package com.nistra.demy.platform.scheduling.application.internal.queryservices;


import com.nistra.demy.platform.institution.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.scheduling.domain.model.aggregates.WeeklySchedule;
import com.nistra.demy.platform.scheduling.domain.model.entities.Schedule;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetAllWeeklySchedulesQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetSchedulesByTeacherIdQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetWeeklyScheduleByIdQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetWeeklyScheduleByNameQuery;
import com.nistra.demy.platform.scheduling.domain.services.WeeklyScheduleQueryService;
import com.nistra.demy.platform.scheduling.infrastructure.persistence.jpa.repositories.WeeklyScheduleRepository;
import com.nistra.demy.platform.scheduling.infrastructure.persistence.jpa.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Weekly Schedule Query Service Implementation
 * <p>This class implements the weekly schedule query service interface and provides the business logic for handling weekly schedule queries such as retrieving all weekly schedules, finding weekly schedules by ID or name, and retrieving schedules by teacher ID.</p>
 */
@Service
public class WeeklyScheduleQueryServiceImpl implements WeeklyScheduleQueryService {

    private final WeeklyScheduleRepository weeklyScheduleRepository;
    private final ScheduleRepository scheduleRepository;
    private final ExternalIamService externalIamService;

    /**
     * Constructor that initializes the service with the required repositories.
     * @param weeklyScheduleRepository The weekly schedule repository.
     * @param scheduleRepository The schedule repository.
     */
    public WeeklyScheduleQueryServiceImpl(WeeklyScheduleRepository weeklyScheduleRepository, ScheduleRepository scheduleRepository, ExternalIamService externalIamService) { // [MODIFICADO]
        this.weeklyScheduleRepository = weeklyScheduleRepository;
        this.scheduleRepository= scheduleRepository;
        this.externalIamService = externalIamService; // Inyectar ACL
    }

    /**
     * This method is used to handle retrieving all weekly schedules.
     * @param query The get all weekly schedules query.
     * @return A list of all weekly schedules in the system.
     * @see GetAllWeeklySchedulesQuery
     * @see WeeklySchedule
     */
    @Override
    public List<WeeklySchedule> handle(GetAllWeeklySchedulesQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("No academy context found for the current user"));

        return weeklyScheduleRepository.findAllByAcademyId(academyId);
    }
    /**
     * This method is used to handle retrieving a weekly schedule by its ID.
     * @param query The get weekly schedule by ID query containing the weekly schedule ID.
     * @return An optional with the weekly schedule if found, otherwise an empty optional.
     * @see GetWeeklyScheduleByIdQuery
     * @see WeeklySchedule
     */
    @Override
    public Optional<WeeklySchedule> handle(GetWeeklyScheduleByIdQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("No academy context found for the current user"));

        return weeklyScheduleRepository.findById(query.weeklyScheduleId())
                .filter(schedule -> schedule.getAcademyId().equals(academyId)); // AÑADIR FILTRO DE PROPIEDAD
    }

    /**
     * This method is used to handle retrieving a weekly schedule by its name.
     * @param query The get weekly schedule by name query containing the weekly schedule name.
     * @return An optional with the weekly schedule if found, otherwise an empty optional.
     * @see GetWeeklyScheduleByNameQuery
     * @see WeeklySchedule
     */
    @Override
    public Optional<WeeklySchedule> handle(GetWeeklyScheduleByNameQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("No academy context found for the current user"));

        return weeklyScheduleRepository.findByNameAndAcademyId(query.name(), academyId);
    }

    /**
     * This method is used to handle retrieving schedules by teacher ID.
     * @param query The get schedules by teacher ID query containing the teacher ID.
     * @return A list of schedules associated with the specified teacher.
     * @see GetSchedulesByTeacherIdQuery
     * @see Schedule
     */
    @Override
    public List<Schedule> handle(GetSchedulesByTeacherIdQuery query){
        return scheduleRepository.findByTeacherId(query.teacherId());
    }


}