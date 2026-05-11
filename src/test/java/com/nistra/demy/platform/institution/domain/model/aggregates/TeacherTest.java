package com.nistra.demy.platform.institution.domain.model.aggregates;

import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterTeacherCommand;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    @DisplayName("Should correctly create a teacher from RegisterTeacherCommand")
    void shouldCreateTeacherSuccessfully() {
        // Arrange
        PersonName personName = new PersonName("Sofia", "Mia");
        EmailAddress emailAddress = new EmailAddress("sofiamia@gmail.com");
        PhoneNumber phoneNumber = new PhoneNumber("+51", "911222333");
        RegisterTeacherCommand command = new RegisterTeacherCommand(personName,emailAddress, phoneNumber);

        UserId userId = new UserId(20L);
        AcademyId academyId = new AcademyId(5L);

        // Act
        Teacher teacher = new Teacher(command, userId, academyId);

        // Assert
        assertNotNull(teacher);
        assertEquals("Sofia", teacher.getPersonName().firstName());
        assertEquals("Mia", teacher.getPersonName().lastName());
        assertEquals("+51", teacher.getPhoneNumber().countryCode());
        assertEquals("911222333", teacher.getPhoneNumber().phone());
        assertEquals(20L, teacher.getUserId().userId());
        assertEquals(5L, teacher.getAcademyId().academyId());
    }

}