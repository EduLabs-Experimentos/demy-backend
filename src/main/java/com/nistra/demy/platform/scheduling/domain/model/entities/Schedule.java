package com.nistra.demy.platform.scheduling.domain.model.entities;

import com.nistra.demy.platform.scheduling.domain.model.aggregates.WeeklySchedule;
import com.nistra.demy.platform.scheduling.domain.model.valueobjects.ClassroomId;
import com.nistra.demy.platform.scheduling.domain.model.valueobjects.CourseId;
import com.nistra.demy.platform.scheduling.domain.model.valueobjects.DayOfWeek;
import com.nistra.demy.platform.scheduling.domain.model.valueobjects.TimeRange;
import com.nistra.demy.platform.shared.domain.model.entities.AuditableModel;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Schedule extends AuditableModel {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startTime", column = @Column(name = "start_time")),
            @AttributeOverride(name = "endTime", column = @Column(name = "end_time"))
    })
    private TimeRange timeRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "course_id"))
    })
    private CourseId courseId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "classroom_id"))
    })
    private ClassroomId classroomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private WeeklySchedule weeklySchedule;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "teacher_id"))
    })
    private UserId teacherId;

    /**
     * Default constructor
     */
    public Schedule() {}

    /**
     * Constructor with TimeRange
     * @param timeRange Time range
     * @param dayOfWeek Day of week
     * @param courseId Course ID
     * @param classroomId Classroom ID
     * @param teacherId Teacher ID
     */
    public Schedule(TimeRange timeRange, DayOfWeek dayOfWeek, CourseId courseId, ClassroomId classroomId, UserId teacherId) {
        this.timeRange = timeRange;
        this.dayOfWeek = dayOfWeek;
        this.courseId = courseId;
        this.classroomId = classroomId;
        this.teacherId = teacherId;
        validateSchedule();
    }

    /**
     * Constructor with string times
     * @param startTime Start time
     * @param endTime End time
     * @param dayOfWeek Day of week
     * @param courseId Course ID
     * @param classroomId Classroom ID
     * @param teacherId Teacher ID
     */
    public Schedule(String startTime, String endTime, DayOfWeek dayOfWeek, Long courseId, Long classroomId, Long teacherId) {
        this(new TimeRange(startTime, endTime), dayOfWeek, new CourseId(courseId), new ClassroomId(classroomId), new UserId(teacherId));
    }

    /**
     * Update schedule
     * @param timeRange Time range
     * @param dayOfWeek Day of week
     * @param courseId Course ID
     * @param classroomId Classroom ID
     * @param teacherId Teacher ID
     */
    public void updateSchedule(TimeRange timeRange, DayOfWeek dayOfWeek, CourseId courseId, ClassroomId classroomId, UserId teacherId) {
        this.timeRange = timeRange;
        this.dayOfWeek = dayOfWeek;
        this.courseId = courseId;
        this.classroomId = classroomId;
        this.teacherId = teacherId;
        validateSchedule();
    }

    /**
     * Update schedule with string times
     * @param startTime Start time
     * @param endTime End time
     * @param dayOfWeek Day of week
     * @param courseId Course ID
     * @param classroomId Classroom ID
     * @param teacherId Teacher ID
     */
    public void updateSchedule(String startTime, String endTime, DayOfWeek dayOfWeek, Long courseId, Long classroomId, Long teacherId) {
        updateSchedule(new TimeRange(startTime, endTime), dayOfWeek, new CourseId(courseId), new ClassroomId(classroomId), new UserId(teacherId));
    }
    public void updateSchedule(Long classroomId, String startTime, String endTime, DayOfWeek dayOfWeek) {
        this.timeRange= new TimeRange(startTime,endTime);
        this.dayOfWeek = dayOfWeek;
        this.classroomId = new ClassroomId(classroomId);
    }

    public void setWeeklySchedule(WeeklySchedule weeklySchedule) { this.weeklySchedule = weeklySchedule; }

    /**
     * Checks if this schedule conflicts with another schedule
     * @param other Other schedule
     * @return True if conflicts, false otherwise
     */
    public boolean conflictsWith(Schedule other) {
        if (other == null) return false;

        return dayOfWeek == other.dayOfWeek &&
                classroomId.equals(other.classroomId) &&
                timeRange.overlapsWith(other.timeRange);
    }


    private void validateSchedule() {
        if (timeRange == null) {
            throw new IllegalArgumentException("Time range must not be null");
        }
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week must not be null");
        }
        if (courseId == null) {
            throw new IllegalArgumentException("Course ID must not be null");
        }
        if (classroomId == null) {
            throw new IllegalArgumentException("Classroom ID must not be null");
        }
        if (teacherId == null) {
            throw new IllegalArgumentException("Teacher ID must not be null");
        }
    }
}