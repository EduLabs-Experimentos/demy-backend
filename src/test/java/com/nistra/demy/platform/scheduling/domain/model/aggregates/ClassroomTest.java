package com.nistra.demy.platform.scheduling.domain.model.aggregates;

import com.nistra.demy.platform.scheduling.domain.model.commands.CreateClassroomCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.UpdateClassroomCommand;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Classroom aggregate root.
 * Validates construction with commands, update behavior, and lifecycle correctness.
 *
 * @since 2.3.0
 */
class ClassroomTest {

    @Test
    @DisplayName("Should construct Classroom using CreateClassroomCommand and AcademyId with all fields correctly set")
    void shouldCreateClassroomFromCommand() {
        // Arrange
        CreateClassroomCommand command = new CreateClassroomCommand("A-101", 30, "Lima Norte");
        AcademyId academyId = new AcademyId(1L);

        // Act
        Classroom classroom = new Classroom(command, academyId);

        // Assert
        assertNotNull(classroom);
        assertEquals("A-101", classroom.getCode());
        assertEquals(30, classroom.getCapacity());
        assertEquals("Lima Norte", classroom.getCampus());
        assertEquals(1L, classroom.getAcademyId().academyId());
    }

    @Test
    @DisplayName("Should update all fields via UpdateClassroomCommand and return the same instance")
    void shouldUpdateClassroomViaCommand() {
        // Arrange
        Classroom classroom = new Classroom("A-101", 30, "Lima Norte", new AcademyId(1L));
        UpdateClassroomCommand command = new UpdateClassroomCommand(10L, "B-202", 45, "Lima Sur");

        // Act
        Classroom updated = classroom.updateClassroom(command);

        // Assert
        assertSame(classroom, updated, "updateClassroom should return the same instance");
        assertEquals("B-202", classroom.getCode());
        assertEquals(45, classroom.getCapacity());
        assertEquals("Lima Sur", classroom.getCampus());
    }

    @Test
    @DisplayName("Should update all fields via individual parameters and persist AcademyId unchanged")
    void shouldUpdateClassroomViaParameters() {
        // Arrange
        AcademyId academyId = new AcademyId(1L);
        Classroom classroom = new Classroom("A-101", 30, "Lima Norte", academyId);

        // Act
        Classroom updated = classroom.updateClassroom("C-303", 50, "Lima Centro");

        // Assert
        assertSame(classroom, updated);
        assertEquals("C-303", classroom.getCode());
        assertEquals(50, classroom.getCapacity());
        assertEquals("Lima Centro", classroom.getCampus());
        assertEquals(1L, classroom.getAcademyId().academyId(), "AcademyId must remain unchanged after update");
    }

    @Test
    @DisplayName("Default constructor should initialize fields to empty strings and zero capacity")
    void shouldInitializeWithDefaults() {
        // Act
        Classroom classroom = new Classroom();

        // Assert
        assertNotNull(classroom);
        assertEquals("", classroom.getCode());
        assertEquals(0, classroom.getCapacity());
        assertEquals("", classroom.getCampus());
        assertNotNull(classroom.getAcademyId());
    }

    @Test
    @DisplayName("Should construct Classroom with direct parameters and verify all fields")
    void shouldCreateClassroomWithDirectParameters() {
        // Arrange
        AcademyId academyId = new AcademyId(2L);

        // Act
        Classroom classroom = new Classroom("D-404", 20, "Callao", academyId);

        // Assert
        assertEquals("D-404", classroom.getCode());
        assertEquals(20, classroom.getCapacity());
        assertEquals("Callao", classroom.getCampus());
        assertEquals(2L, classroom.getAcademyId().academyId());
    }
}
