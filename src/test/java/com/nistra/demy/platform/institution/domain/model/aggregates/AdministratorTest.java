package com.nistra.demy.platform.institution.domain.model.aggregates;

import com.nistra.demy.platform.institution.domain.model.commands.RegisterAdministratorCommand;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdministratorTest {

    @Test
    @DisplayName("Should create administrator from command with all fields set")
    void shouldCreateAdministratorFromCommand() {
        // Arrange
        RegisterAdministratorCommand command = new RegisterAdministratorCommand(
                new PersonName("Juan", "Perez"),
                new PhoneNumber("+51", "999888777"),
                new DniNumber("12345678"),
                new UserId(100L)
        );

        // Act
        Administrator admin = new Administrator(command);

        // Assert
        assertNotNull(admin);
        assertEquals("Juan", admin.getPersonName().firstName());
        assertEquals("Perez", admin.getPersonName().lastName());
        assertEquals("+51", admin.getPhoneNumber().countryCode());
        assertEquals("999888777", admin.getPhoneNumber().phone());
        assertEquals("12345678", admin.getDniNumber().dniNumber());
        assertEquals(100L, admin.getUserId().userId());
        assertNotNull(admin.getAcademyId());
        assertNull(admin.getAcademyId().academyId());
    }

    @Test
    @DisplayName("Should publish AdministratorRegisteredEvent when registerAdministrator is called")
    void shouldPublishEventOnRegisterAdministrator() {
        // Arrange
        Administrator admin = new Administrator(
                new PersonName("Maria", "Lopez"),
                new PhoneNumber("+51", "911222333"),
                new DniNumber("87654321"),
                new UserId(200L)
        );

        // Act
        admin.registerAdministrator(1L, 200L);

        // Assert
        Collection<?> events = (Collection<?>) ReflectionTestUtils.invokeMethod(admin, "domainEvents");
        assertEquals(1, events.size());
    }

//    @Test
//    @DisplayName("Should associate academy once and throw on reassignment")
//    void shouldAssociateAndDisassociateAcademy() {
//        // Arrange
//        Administrator admin = new Administrator(
//                new PersonName("Carlos", "Garcia"),
//                new PhoneNumber("+51", "955444333"),
//                new DniNumber("11223344"),
//                new UserId(300L)
//        );
//
//        AcademyId mockAcademyId = mock(AcademyId.class);
//        when(mockAcademyId.academyId()).thenReturn(0L);
//        ReflectionTestUtils.setField(admin, "academyId", mockAcademyId);
//
//        AcademyId academy1 = new AcademyId(10L);
//
//        // Act
//        admin.associateAcademy(academy1);
//
//        // Assert - after associate, academyId should be academy1 (the real object)
//        assertEquals(10L, admin.getAcademyId().academyId());
//
//        // Arrange
//        AcademyId academy2 = new AcademyId(20L);
//
//        // Act & Assert - should throw when trying to associate again
//        assertThrows(IllegalStateException.class, () -> admin.associateAcademy(academy2));
//
//        // Act - disassociate
//        admin.disassociateAcademy(academy1);
//
//        // Assert - after disassociate, academyId should still exist but be "empty" (new AcademyId() which has null)
//        assertNotNull(admin.getAcademyId());
//    }
}