package com.nistra.demy.platform.scheduling.domain.model.aggregates;

import com.nistra.demy.platform.scheduling.domain.model.commands.CreateWeeklyScheduleCommand;
import com.nistra.demy.platform.scheduling.domain.model.entities.Schedule;
import com.nistra.demy.platform.scheduling.domain.model.valueobjects.DayOfWeek;
import com.nistra.demy.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class WeeklySchedule extends AuditableAbstractAggregateRoot<WeeklySchedule> {

    private String name;

    @Embedded
    private AcademyId academyId;

    @OneToMany(mappedBy = "weeklySchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Schedule> schedules;

    /**
     * Default constructor
     */
    public WeeklySchedule() {
        this.name = "";
        this.schedules = new ArrayList<>();
        this.academyId = new AcademyId();
    }

    /**
     * Constructor with name
     * @param name Weekly schedule name
     */
    public WeeklySchedule(String name, AcademyId academyId) {
        this.name = name;
        this.schedules = new ArrayList<>();
        this.academyId = academyId;
    }

    /**
     * Constructor with command
     * @param command Create weekly schedule command
     */
    public WeeklySchedule(CreateWeeklyScheduleCommand command, AcademyId academyId) {
        this.name = command.name();
        this.academyId = academyId;
        this.schedules = new ArrayList<>();
    }

    /**
     * Update name
     * @param name New name
     */
    public void updateName(String name) {
        this.name = name;
    }

    /**
     * Add schedule with parameters.
     *
     * @param startTime Start time of the schedule
     * @param endTime End time of the schedule
     * @param dayOfWeek Day of the week for the schedule
     * @param courseId ID of the course
     * @param classroomId ID of the classroom
     * @param teacherId ID of the teacher
     */
    public void addSchedule(String startTime, String endTime, DayOfWeek dayOfWeek, Long courseId, Long classroomId, Long teacherId) {
        var schedule = new Schedule(startTime, endTime, dayOfWeek, courseId, classroomId, teacherId);

        schedule.setWeeklySchedule(this);
        schedules.add(schedule);
    }

    /**
     * Remove a schedule from the list by its ID.
     *
     * @param scheduleId ID of the schedule to remove
     */
    public void removeSchedule(Long scheduleId) {
        schedules.removeIf(s -> s.getId().equals(scheduleId));
    }

}