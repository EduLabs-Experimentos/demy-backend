package com.nistra.demy.platform.institution.domain.model.aggregates;

import com.nistra.demy.platform.institution.domain.model.commands.RegisterAcademyCommand;
import com.nistra.demy.platform.institution.domain.model.valueobjects.*;
import com.nistra.demy.platform.shared.domain.model.valueobjects.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AcademyTest {

    @Test
    @DisplayName("Should correctly create an academy using the main constructor")
    void shouldCreateAcademyWithConstructor() {
        // Arrange
        AcademyName name = new AcademyName("Instituto Nistra");
        AcademyDescription description = new AcademyDescription("Academia dedicada a la formacion tecnologica.");
        StreetAddress address = new StreetAddress("Av. Arequipa 1234", "Miraflores", "Lima", "Lima");
        EmailAddress email = new EmailAddress("info@nistra.com");
        PhoneNumber phone = new PhoneNumber("+51", "987654321");
        Ruc ruc = new Ruc("10456789123");

        // Act
        Academy academy = new Academy(name, description, address, email, phone, ruc);

        // Assert
        assertNotNull(academy);
        assertEquals("Instituto Nistra", academy.getAcademyName().name());
        assertEquals("Academia dedicada a la formacion tecnologica.", academy.getAcademyDescription().description());
        assertEquals("Av. Arequipa 1234", academy.getStreetAddress().street());
        assertEquals("Miraflores", academy.getStreetAddress().district());
        assertEquals("Lima", academy.getStreetAddress().province());
        assertEquals("Lima", academy.getStreetAddress().department());
        assertEquals("info@nistra.com", academy.getEmailAddress().email());
        assertEquals("+51", academy.getPhoneNumber().countryCode());
        assertEquals("987654321", academy.getPhoneNumber().phone());
        assertEquals("10456789123", academy.getRuc().ruc());
        assertNull(academy.getAdministratorId());
    }

    @Test
    @DisplayName("Should correctly create an academy using the RegisterAcademyCommand")
    void shouldCreateAcademyFromCommand() {
        // Arrange
        RegisterAcademyCommand command = new RegisterAcademyCommand(
                new AcademyName("Academia Demy"),
                new AcademyDescription("Formacion profesional para jovenes."),
                new StreetAddress("Jr. Los Robles 567", "San Isidro", "Lima", "Lima"),
                new EmailAddress("contacto@demy.com"),
                new PhoneNumber("+51", "999999999"),
                new Ruc("10987654321"),
                new AdministratorId(1001L)
        );

        // Act
        Academy academy = new Academy(command);

        // Assert
        assertNotNull(academy);
        assertEquals("Academia Demy", academy.getAcademyName().name());
        assertEquals("Formacion profesional para jovenes.", academy.getAcademyDescription().description());
        assertEquals("Jr. Los Robles 567", academy.getStreetAddress().street());
        assertEquals("contacto@demy.com", academy.getEmailAddress().email());
        assertEquals("10987654321", academy.getRuc().ruc());
        assertNull(academy.getAdministratorId());
    }

    @Test
    @DisplayName("Should allow assigning an administrator only once and throw an exception if reassigned")
    void shouldAssignAdministratorAndThrowIfAlreadyAssigned() {
        // Arrange
        Academy academy = new Academy(
                new AcademyName("Test Academy"),
                new AcademyDescription("Academia de prueba."),
                new StreetAddress("Calle Falsa 123", "Santiago", "Cusco", "Cusco"),
                new EmailAddress("test@academy.com"),
                new PhoneNumber("+51", "911111111"),
                new Ruc("10765432109")
        );
        AdministratorId admin1 = new AdministratorId(1L);

        // Act
        academy.assignAdministrator(admin1);

        // Assert
        assertTrue(academy.getAdministratorId().isAssigned());
        assertEquals(1L, academy.getAdministratorId().administratorId());

        // Arrange
        AdministratorId admin2 = new AdministratorId(2L);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            academy.assignAdministrator(admin2);
        });
        assertEquals("Administrator is already assigned to this academy", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw an exception if the AdministratorId is invalid (<= 0)")
    void shouldThrowExceptionForInvalidAdministratorId() {
        // Arrange
        Long invalidId = 0L;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new AdministratorId(invalidId);
        });
        assertEquals("Administrator ID must be greater than zero if provided", exception.getMessage());
    }

    @Test
    @DisplayName("Should return false when the AdministratorId is not assigned (null)")
    void shouldReturnFalseWhenAdministratorNotAssigned() {
        // Arrange
        AdministratorId unassigned = new AdministratorId();

        // Act & Assert
        assertFalse(unassigned.isAssigned());
        assertNull(unassigned.administratorId());
    }
}