package com.nistra.demy.platform.scheduling.domain.model.entities;

import com.nistra.demy.platform.scheduling.domain.model.aggregates.WeeklySchedule;
import com.nistra.demy.platform.scheduling.domain.model.valueobjects.*;
import com.nistra.demy.platform.shared.domain.model.valueobjects.*;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Schedule entity using the Arrange-Act-Assert (AAA) pattern.
 * Verifies construction, update behavior, validation, and conflict detection.
 *
 * @author Paul Sulca
 * @since 1.0.0
 */
class ScheduleTest {

    @Test
    @DisplayName("Should correctly create a Schedule using the main constructor")
    void shouldCreateScheduleSuccessfully() {
        // Arrange
        TimeRange timeRange = new TimeRange(LocalTime.of(9, 0), LocalTime.of(11, 0));
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        CourseId courseId = new CourseId(100L);
        ClassroomId classroomId = new ClassroomId(200L);
        UserId teacherId = new UserId(10L);

        // Act
        Schedule schedule = new Schedule(timeRange, dayOfWeek, courseId, classroomId, teacherId);

        // Assert
        assertNotNull(schedule);
        assertEquals(LocalTime.of(9, 0), schedule.getTimeRange().startTime());
        assertEquals(LocalTime.of(11, 0), schedule.getTimeRange().endTime());
        assertEquals(DayOfWeek.MONDAY, schedule.getDayOfWeek());
        assertEquals(100L, schedule.getCourseId().id());
        assertEquals(200L, schedule.getClassroomId().id());
        assertEquals(10L, schedule.getTeacherId().userId());
    }

    @Test
    @DisplayName("Should correctly create a Schedule using string-based constructor")
    void shouldCreateScheduleUsingStringConstructor() {
        // Arrange & Act
        Schedule schedule = new Schedule("08:00", "10:00", DayOfWeek.TUESDAY, 1L, 2L, 3L);

        // Assert
        assertEquals(LocalTime.of(8, 0), schedule.getTimeRange().startTime());
        assertEquals(LocalTime.of(10, 0), schedule.getTimeRange().endTime());
        assertEquals(DayOfWeek.TUESDAY, schedule.getDayOfWeek());
        assertEquals(1L, schedule.getCourseId().id());
        assertEquals(2L, schedule.getClassroomId().id());
        assertEquals(3L, schedule.getTeacherId().userId());
    }

    @Test
    @DisplayName("Should throw exception when creating a Schedule with null fields")
    void shouldThrowExceptionWhenFieldsAreNull() {
        // Arrange
        TimeRange timeRange = new TimeRange(LocalTime.of(9, 0), LocalTime.of(10, 0));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new Schedule(null, DayOfWeek.MONDAY, new CourseId(1L), new ClassroomId(1L), new UserId(1L)));

        assertThrows(IllegalArgumentException.class,
                () -> new Schedule(timeRange, null, new CourseId(1L), new ClassroomId(1L), new UserId(1L)));

        assertThrows(IllegalArgumentException.class,
                () -> new Schedule(timeRange, DayOfWeek.MONDAY, null, new ClassroomId(1L), new UserId(1L)));

        assertThrows(IllegalArgumentException.class,
                () -> new Schedule(timeRange, DayOfWeek.MONDAY, new CourseId(1L), null, new UserId(1L)));

        assertThrows(IllegalArgumentException.class,
                () -> new Schedule(timeRange, DayOfWeek.MONDAY, new CourseId(1L), new ClassroomId(1L), null));
    }

    @Test
    @DisplayName("Should update schedule information successfully using TimeRange")
    void shouldUpdateScheduleSuccessfully() {
        // Arrange
        Schedule schedule = new Schedule("09:00", "11:00", DayOfWeek.MONDAY, 1L, 1L, 1L);
        TimeRange updatedRange = new TimeRange(LocalTime.of(14, 0), LocalTime.of(16, 0));
        DayOfWeek updatedDay = DayOfWeek.FRIDAY;
        CourseId updatedCourse = new CourseId(2L);
        ClassroomId updatedClassroom = new ClassroomId(3L);
        UserId updatedTeacher = new UserId(4L);

        // Act
        schedule.updateSchedule(updatedRange, updatedDay, updatedCourse, updatedClassroom, updatedTeacher);

        // Assert
        assertEquals(LocalTime.of(14, 0), schedule.getTimeRange().startTime());
        assertEquals(LocalTime.of(16, 0), schedule.getTimeRange().endTime());
        assertEquals(DayOfWeek.FRIDAY, schedule.getDayOfWeek());
        assertEquals(2L, schedule.getCourseId().id());
        assertEquals(3L, schedule.getClassroomId().id());
        assertEquals(4L, schedule.getTeacherId().userId());
    }

    @Test
    @DisplayName("Should detect conflict when two schedules overlap on same day and classroom")
    void shouldDetectConflictWithAnotherSchedule() {
        // Arrange
        Schedule schedule1 = new Schedule("08:00", "10:00", DayOfWeek.MONDAY, 1L, 1L, 1L);
        Schedule schedule2 = new Schedule("09:00", "11:00", DayOfWeek.MONDAY, 2L, 1L, 2L);

        // Act
        boolean conflict = schedule1.conflictsWith(schedule2);

        // Assert
        assertTrue(conflict, "Schedules with overlapping time ranges on same classroom and day should conflict");
    }

    @Test
    @DisplayName("Should not detect conflict for different classroom or day")
    void shouldNotConflictWhenDifferentDayOrClassroom() {
        // Arrange
        Schedule mondaySchedule = new Schedule("08:00", "10:00", DayOfWeek.MONDAY, 1L, 1L, 1L);
        Schedule tuesdaySchedule = new Schedule("08:00", "10:00", DayOfWeek.TUESDAY, 1L, 1L, 1L);
        Schedule differentClassroom = new Schedule("09:00", "11:00", DayOfWeek.MONDAY, 1L, 2L, 1L);

        // Act & Assert
        assertFalse(mondaySchedule.conflictsWith(tuesdaySchedule));
        assertFalse(mondaySchedule.conflictsWith(differentClassroom));
    }

    @Test
    @DisplayName("Should throw exception when creating TimeRange with invalid times")
    void shouldThrowExceptionForInvalidTimeRange() {
        // Arrange, Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TimeRange(LocalTime.of(10, 0), LocalTime.of(9, 0)));

        assertEquals("Start time must be before end time", exception.getMessage());
    }

    @Test
    @DisplayName("Should associate schedule with WeeklySchedule correctly")
    void shouldAssociateWithWeeklySchedule() {
        // Arrange
        WeeklySchedule weeklySchedule = new WeeklySchedule("Semana 1", new AcademyId(1L));
        Schedule schedule = new Schedule("08:00", "10:00", DayOfWeek.MONDAY, 1L, 1L, 1L);

        // Act
        schedule.setWeeklySchedule(weeklySchedule);

        // Assert
        assertEquals(weeklySchedule, schedule.getWeeklySchedule());
    }

    @Test
    @DisplayName("Should update schedule partially using classroom and time range")
    void shouldUpdateSchedulePartially() {
        // Arrange
        Schedule schedule = new Schedule("08:00", "10:00", DayOfWeek.MONDAY, 1L, 1L, 1L);

        // Act
        schedule.updateSchedule(2L, "11:00", "13:00", DayOfWeek.WEDNESDAY);

        // Assert
        assertEquals(DayOfWeek.WEDNESDAY, schedule.getDayOfWeek());
        assertEquals(LocalTime.of(11, 0), schedule.getTimeRange().startTime());
        assertEquals(LocalTime.of(13, 0), schedule.getTimeRange().endTime());
        assertEquals(2L, schedule.getClassroomId().id());
    }
}