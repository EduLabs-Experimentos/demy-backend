package com.nistra.demy.platform.scheduling.domain.model.aggregates;

import com.nistra.demy.platform.scheduling.domain.model.commands.CreateCourseCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.UpdateCourseCommand;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Course aggregate root.
 * Validates construction with commands, update behavior, and lifecycle correctness.
 *
 * @since 2.3.0
 */
class CourseTest {

    @Test
    @DisplayName("Should construct Course using CreateCourseCommand and AcademyId with all fields correctly set")
    void shouldCreateCourseFromCommand() {
        // Arrange
        CreateCourseCommand command = new CreateCourseCommand("Mathematics", "MATH-101", "Basic algebra and geometry");
        AcademyId academyId = new AcademyId(1L);

        // Act
        Course course = new Course(command, academyId);

        // Assert
        assertNotNull(course);
        assertEquals("Mathematics", course.getName());
        assertEquals("MATH-101", course.getCode());
        assertEquals("Basic algebra and geometry", course.getDescription());
        assertEquals(1L, course.getAcademyId().academyId());
    }

    @Test
    @DisplayName("Should update all fields via UpdateCourseCommand and return the same instance")
    void shouldUpdateCourseViaCommand() {
        // Arrange
        Course course = new Course("Mathematics", "MATH-101", "Basic algebra", new AcademyId(1L));
        UpdateCourseCommand command = new UpdateCourseCommand(10L, "Advanced Mathematics", "MATH-201", "Linear algebra and calculus");

        // Act
        Course updated = course.updateCourse(command);

        // Assert
        assertSame(course, updated, "updateCourse should return the same instance");
        assertEquals("Advanced Mathematics", course.getName());
        assertEquals("MATH-201", course.getCode());
        assertEquals("Linear algebra and calculus", course.getDescription());
    }

    @Test
    @DisplayName("Should update all fields via individual parameters and persist AcademyId unchanged")
    void shouldUpdateCourseViaParameters() {
        // Arrange
        AcademyId academyId = new AcademyId(1L);
        Course course = new Course("Physics", "PHYS-101", "Introduction to mechanics", academyId);

        // Act
        Course updated = course.updateCourse("Advanced Physics", "PHYS-201", "Electromagnetism and optics");

        // Assert
        assertSame(course, updated);
        assertEquals("Advanced Physics", course.getName());
        assertEquals("PHYS-201", course.getCode());
        assertEquals("Electromagnetism and optics", course.getDescription());
        assertEquals(1L, course.getAcademyId().academyId(), "AcademyId must remain unchanged after update");
    }

    @Test
    @DisplayName("Default constructor should initialize all fields to empty strings")
    void shouldInitializeWithDefaults() {
        // Act
        Course course = new Course();

        // Assert
        assertNotNull(course);
        assertEquals("", course.getName());
        assertEquals("", course.getCode());
        assertEquals("", course.getDescription());
        assertNotNull(course.getAcademyId());
    }

    @Test
    @DisplayName("Should construct Course with direct parameters and verify all fields")
    void shouldCreateCourseWithDirectParameters() {
        // Arrange
        AcademyId academyId = new AcademyId(2L);

        // Act
        Course course = new Course("Chemistry", "CHEM-101", "Organic chemistry", academyId);

        // Assert
        assertEquals("Chemistry", course.getName());
        assertEquals("CHEM-101", course.getCode());
        assertEquals("Organic chemistry", course.getDescription());
        assertEquals(2L, course.getAcademyId().academyId());
    }
}
