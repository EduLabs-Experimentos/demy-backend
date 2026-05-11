package com.nistra.demy.platform.scheduling.interfaces.rest.transform;

import com.nistra.demy.platform.scheduling.domain.model.aggregates.WeeklySchedule;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.WeeklyScheduleResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeeklyScheduleResourceFromEntityAssembler {

    private final ScheduleResourceFromEntityAssembler scheduleResourceAssembler; // NUEVA DEPENDENCIA

    public WeeklyScheduleResourceFromEntityAssembler(ScheduleResourceFromEntityAssembler scheduleResourceAssembler) {
        this.scheduleResourceAssembler = scheduleResourceAssembler;
    }

    /**
     * Converts a WeeklySchedule entity to a WeeklyScheduleResource
     * @param entity the WeeklySchedule entity
     * @return the WeeklyScheduleResource
     */
    public WeeklyScheduleResource toResourceFromEntity(WeeklySchedule entity) { // MODIFICADO: Ya no es static
        var schedules = entity.getSchedules().stream()
                .map(scheduleResourceAssembler::toResourceFromEntity) // MODIFICADO para usar el bean inyectado
                .toList();

        return new WeeklyScheduleResource(
                entity.getId(),
                entity.getName(),
                schedules
        );
    }

    public static List<WeeklyScheduleResource> toResourcesFromEntities(List<WeeklySchedule> entities, WeeklyScheduleResourceFromEntityAssembler assembler) {
        return entities.stream()
                .map(assembler::toResourceFromEntity)
                .toList();
    }
}