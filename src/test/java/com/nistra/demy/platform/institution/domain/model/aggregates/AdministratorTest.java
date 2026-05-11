package com.nistra.demy.platform.institution.domain.model.aggregates;

import com.nistra.demy.platform.institution.domain.model.events.AdministratorRegisteredEvent;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import io.cucumber.java.bs.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Administrator aggregate using the Arrange-Act-Assert (AAA) pattern.
 * Compatible with the actual VO implementations: personName, PhoneNumber, dniNumber, userId.
 */
class AdministratorTest {

    private PersonName personName;
    private PhoneNumber phoneNumber;
    private DniNumber dniNumber;
    private UserId userId;


    @BeforeEach
    void setUp() {
        personName = new PersonName("Carlos", "Mendoza");
        phoneNumber = new PhoneNumber("+51", "987654321");
        dniNumber = new DniNumber("76543210");
        userId = new UserId(10L);
    }

    @Test
    @DisplayName("Should correctly create an administrator and initialize with an empty AcademyId")
    void shouldCreateAdministratorSuccessfully() {
        // Arrange & Act
        Administrator admin = new Administrator(personName,phoneNumber,dniNumber, userId);

        //Assert
        assertNotNull(admin);
        assertEquals("Carlos", admin.getPersonName().firstName());
        assertEquals("76543210", admin.getDniNumber().dniNumber());
        assertEquals(10L, admin.getUserId().userId());
        assertNotNull(admin.getAcademyId(), "AcademyId should be initialized");
        assertTrue(admin.getAcademyId().academyId() == null || admin.getAcademyId().academyId() == 0L);

    }

    @Test
    @DisplayName("Should register administrator and add a domain event")
    void shouldRegisterAdministratorAndAddEvent() {
        // Arrange
        Administrator admin = new Administrator(personName,phoneNumber,dniNumber, userId);
        Long expectedAcademyId = 5L;
        Long expectedUserId = 10L;



        // Act
        admin.registerAdministrator(expectedAcademyId,expectedUserId);

        //Assert
        Collection<Object> events = admin.getDomainEvents();

        assertEquals(1, events.size(), "Debería haber exactamente 1 evento registrado");

        AdministratorRegisteredEvent event = (AdministratorRegisteredEvent) events.iterator().next();

        assertEquals(expectedAcademyId, event.getAcademyId());
        assertEquals(expectedUserId, event.getUserId());
    }


    @Test
    @DisplayName("Should associate an academy successfully when not previously associated")
    void shouldAssociateAcademySuccessfully() {
        // Arrange
        Administrator admin = new Administrator(personName, phoneNumber, dniNumber, userId);
        AcademyId newAcademyId = new AcademyId(5L);

        // Act
        admin.associateAcademy(newAcademyId);

        // Assert
        assertEquals(5L, admin.getAcademyId().academyId());
    }

    @Test
    @DisplayName("Should throw an exception when trying to associate an academy if already associated")
    void shouldThrowExceptionWhenAlreadyAssociated() {
        // Arrange
        Administrator admin = new Administrator(personName, phoneNumber, dniNumber, userId);
        admin.associateAcademy(new AcademyId(5L)); // Primera asociación

        AcademyId anotherAcademyId = new AcademyId(99L);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            admin.associateAcademy(anotherAcademyId);
        });

        assertEquals("Administrator is already associated with an academy.", exception.getMessage());
    }

    @Test
    @DisplayName("Should disassociate an academy successfully if it matches the current one")
    void shouldDisassociateAcademySuccessfully() {
        // Arrange
        Administrator admin = new Administrator(personName, phoneNumber, dniNumber, userId);
        AcademyId academyId = new AcademyId(5L);
        admin.associateAcademy(academyId);

        // Act
        admin.disassociateAcademy(academyId);

        // Assert
        assertTrue(admin.getAcademyId().academyId() == null || admin.getAcademyId().academyId() == 0L);
    }

    @Test
    @DisplayName("Should throw an exception when trying to disassociate an academy that is not the current one")
    void shouldThrowExceptionWhenDisassociatingWrongAcademy() {
        // Arrange
        Administrator admin = new Administrator(personName, phoneNumber, dniNumber, userId);
        admin.associateAcademy(new AcademyId(5L));

        AcademyId wrongAcademyId = new AcademyId(99L); // ID distinto

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            admin.disassociateAcademy(wrongAcademyId);
        });

        assertEquals("Administrator is not associated with the specified academy.", exception.getMessage());
    }


}