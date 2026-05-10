package com.nistra.demy.platform.scheduling.domain.model.aggregates;

import com.nistra.demy.platform.scheduling.domain.model.commands.CreateWeeklyScheduleCommand;
import com.nistra.demy.platform.scheduling.domain.model.entities.Schedule;
import com.nistra.demy.platform.scheduling.domain.model.valueobjects.DayOfWeek;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the WeeklySchedule aggregate root.
 * Tests core schedule management logic: adding/removing schedules,
 * proper bidirectional associations, update operations, and edge cases.
 *
 * @since 2.3.0
 */
class WeeklyScheduleTest {

    private static final AcademyId ACADEMY_ID = new AcademyId(1L);

    private void setScheduleId(Schedule schedule, Long id) {
        try {
            Field idField = schedule.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(schedule, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set schedule ID via reflection", e);
        }
    }

    @Test
    @DisplayName("Should construct WeeklySchedule with name and AcademyId and initialize empty schedules list")
    void shouldCreateWeeklyScheduleWithEmptySchedules() {
        // Act
        WeeklySchedule schedule = new WeeklySchedule("Semana 1", ACADEMY_ID);

        // Assert
        assertNotNull(schedule);
        assertEquals("Semana 1", schedule.getName());
        assertEquals(1L, schedule.getAcademyId().academyId());
        assertNotNull(schedule.getSchedules());
        assertTrue(schedule.getSchedules().isEmpty());
    }

    @Test
    @DisplayName("Should construct WeeklySchedule via CreateWeeklyScheduleCommand")
    void shouldCreateWeeklyScheduleFromCommand() {
        // Arrange
        CreateWeeklyScheduleCommand command = new CreateWeeklyScheduleCommand("Semana Intensiva");

        // Act
        WeeklySchedule schedule = new WeeklySchedule(command, ACADEMY_ID);

        // Assert
        assertEquals("Semana Intensiva", schedule.getName());
        assertEquals(1L, schedule.getAcademyId().academyId());
        assertNotNull(schedule.getSchedules());
        assertTrue(schedule.getSchedules().isEmpty());
    }

    @Test
    @DisplayName("Should add a schedule and verify bidirectional association is correctly set")
    void shouldAddScheduleAndSetBidirectionalAssociation() {
        // Arrange
        WeeklySchedule weekly = new WeeklySchedule("Semana 1", ACADEMY_ID);

        // Act
        weekly.addSchedule("08:00", "10:00", DayOfWeek.MONDAY, 100L, 200L, 300L);

        // Assert
        assertEquals(1, weekly.getSchedules().size());
        Schedule added = weekly.getSchedules().get(0);
        assertNotNull(added);
        assertEquals(LocalTime.of(8, 0), added.getTimeRange().startTime());
        assertEquals(LocalTime.of(10, 0), added.getTimeRange().endTime());
        assertEquals(DayOfWeek.MONDAY, added.getDayOfWeek());
        assertEquals(100L, added.getCourseId().id());
        assertEquals(200L, added.getClassroomId().id());
        assertEquals(300L, added.getTeacherId().userId());
        assertSame(weekly, added.getWeeklySchedule(), "Schedule must reference back to the WeeklySchedule");
    }

    @Test
    @DisplayName("Should add multiple schedules and maintain correct order")
    void shouldAddMultipleSchedules() {
        // Arrange
        WeeklySchedule weekly = new WeeklySchedule("Semana 1", ACADEMY_ID);

        // Act
        weekly.addSchedule("08:00", "10:00", DayOfWeek.MONDAY, 101L, 201L, 301L);
        weekly.addSchedule("10:00", "12:00", DayOfWeek.MONDAY, 102L, 201L, 302L);
        weekly.addSchedule("08:00", "10:00", DayOfWeek.TUESDAY, 103L, 202L, 303L);

        // Assert
        assertEquals(3, weekly.getSchedules().size());
        assertEquals(DayOfWeek.MONDAY, weekly.getSchedules().get(0).getDayOfWeek());
        assertEquals(101L, weekly.getSchedules().get(0).getCourseId().id());
        assertEquals(DayOfWeek.TUESDAY, weekly.getSchedules().get(2).getDayOfWeek());
        weekly.getSchedules().forEach(s ->
                assertSame(weekly, s.getWeeklySchedule(), "Every schedule must reference the parent WeeklySchedule")
        );
    }

    @Test
    @DisplayName("Should remove an existing schedule by its ID")
    void shouldRemoveExistingScheduleById() {
        // Arrange
        WeeklySchedule weekly = new WeeklySchedule("Semana 1", ACADEMY_ID);
        weekly.addSchedule("08:00", "10:00", DayOfWeek.MONDAY, 101L, 201L, 301L);
        weekly.addSchedule("10:00", "12:00", DayOfWeek.WEDNESDAY, 102L, 202L, 302L);
        setScheduleId(weekly.getSchedules().get(0), 1L);
        setScheduleId(weekly.getSchedules().get(1), 2L);

        Long idToRemove = 1L;

        // Act
        weekly.removeSchedule(idToRemove);

        // Assert
        assertEquals(1, weekly.getSchedules().size());
        assertEquals(DayOfWeek.WEDNESDAY, weekly.getSchedules().get(0).getDayOfWeek());
    }

    @Test
    @DisplayName("Should not fail when removing a non-existent schedule ID")
    void shouldNotFailWhenRemovingNonExistentSchedule() {
        // Arrange
        WeeklySchedule weekly = new WeeklySchedule("Semana 1", ACADEMY_ID);
        weekly.addSchedule("08:00", "10:00", DayOfWeek.MONDAY, 101L, 201L, 301L);
        setScheduleId(weekly.getSchedules().get(0), 5L);

        // Act
        weekly.removeSchedule(9999L);

        // Assert
        assertEquals(1, weekly.getSchedules().size(),
                "Removing a non-existent schedule should leave the list unchanged");
    }

    @Test
    @DisplayName("Should update the name of the WeeklySchedule")
    void shouldUpdateName() {
        // Arrange
        WeeklySchedule weekly = new WeeklySchedule("Semana Original", ACADEMY_ID);

        // Act
        weekly.updateName("Semana Modificada");

        // Assert
        assertEquals("Semana Modificada", weekly.getName());
    }

    @Test
    @DisplayName("Should remove schedule from empty list without error")
    void shouldRemoveFromEmptyScheduleList() {
        // Arrange
        WeeklySchedule weekly = new WeeklySchedule("Semana Vacía", ACADEMY_ID);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> weekly.removeSchedule(1L));
        assertTrue(weekly.getSchedules().isEmpty());
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
}