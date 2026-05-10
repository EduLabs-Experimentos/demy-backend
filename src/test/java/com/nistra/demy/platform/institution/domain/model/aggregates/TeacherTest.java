package com.nistra.demy.platform.institution.domain.model.aggregates;

import com.nistra.demy.platform.institution.domain.model.commands.RegisterTeacherCommand;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    @DisplayName("Should create teacher from command with all fields set")
    void shouldCreateTeacherFromCommand() {
        // Arrange
        RegisterTeacherCommand command = new RegisterTeacherCommand(
                new PersonName("Ana", "Torres"),
                new EmailAddress("ana.torres@academy.com"),
                new PhoneNumber("+51", "977666555")
        );
        UserId userId = new UserId(400L);
        AcademyId academyId = new AcademyId(5L);

        // Act
        Teacher teacher = new Teacher(command, userId, academyId);

        // Assert
        assertNotNull(teacher);
        assertEquals("Ana", teacher.getPersonName().firstName());
        assertEquals("Torres", teacher.getPersonName().lastName());
        assertEquals("+51", teacher.getPhoneNumber().countryCode());
        assertEquals("977666555", teacher.getPhoneNumber().phone());
        assertEquals(400L, teacher.getUserId().userId());
        assertEquals(5L, teacher.getAcademyId().academyId());
    }

    @Test
    @DisplayName("Should use JPA default constructor without issues")
    void shouldWorkWithDefaultConstructor() {
        // Arrange
        Teacher teacher = new Teacher();

        // Act

        // Assert
        assertNotNull(teacher);
        assertNull(teacher.getPersonName());
        assertNull(teacher.getPhoneNumber());
        assertNull(teacher.getAcademyId());
        assertNull(teacher.getUserId());
    }
}