package com.nistra.demy.platform.enrollment.domain.model.aggregates;

import com.nistra.demy.platform.enrollment.domain.model.commands.CreateStudentCommand;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.Sex;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Student aggregate using the Arrange-Act-Assert (AAA) pattern.
 * Verifies correct construction, update behavior, and validation.
 */
class StudentTest {

    @Test
    @DisplayName("Should correctly create a student using the main constructor")
    void shouldCreateStudentWithMainConstructor() {
        // Arrange
        String firstName = "Salim";
        String lastName = "Ramirez";
        String dni = "76543210";
        String sex = "MALE";
        LocalDate birthDate = LocalDate.of(2000, 5, 20);
        String street = "Av. Primavera 123";
        String district = "Surco";
        String province = "Lima";
        String department = "Lima";
        String countryCode = "+51";
        String phone = "999888777";
        Long academyId = 10L;
        Long userId = 5L;

        // Act
        Student student = new Student(firstName, lastName, dni, sex, birthDate,
                street, district, province, department, countryCode, phone, academyId, userId);

        // Assert
        assertNotNull(student);
        assertEquals("Salim", student.getPersonName().firstName());
        assertEquals("Ramirez", student.getPersonName().lastName());
        assertEquals("76543210", student.getDni().dniNumber());
        assertEquals(Sex.MALE, student.getSex());
        assertEquals(LocalDate.of(2000, 5, 20), student.getBirthDate());
        assertEquals("Av. Primavera 123", student.getAddress().street());
        assertEquals("+51", student.getPhoneNumber().countryCode());
        assertEquals("999888777", student.getPhoneNumber().phone());
        assertEquals(10L, student.getAcademyId().academyId());
        assertEquals(5L, student.getUserId().userId());
    }

    @Test
    @DisplayName("Should correctly create a student using value objects constructor")
    void shouldCreateStudentWithValueObjects() {
        // Arrange
        PersonName personName = new PersonName("Camila", "Gomez");
        DniNumber dni = new DniNumber("12345678");
        Sex sex = Sex.FEMALE;
        LocalDate birthDate = LocalDate.of(1999, 8, 15);
        StreetAddress address = new StreetAddress("Jr. Los Pinos 345", "Miraflores", "Lima", "Lima");
        PhoneNumber phoneNumber = new PhoneNumber("+51", "987654321");
        AcademyId academyId = new AcademyId(12L);
        UserId userId = new UserId(3L);

        // Act
        Student student = new Student(personName, dni, sex, birthDate, address, phoneNumber, academyId, userId);

        // Assert
        assertEquals("Camila", student.getPersonName().firstName());
        assertEquals("987654321", student.getPhoneNumber().phone());
        assertEquals(Sex.FEMALE, student.getSex());
        assertEquals(12L, student.getAcademyId().academyId());
        assertEquals(3L, student.getUserId().userId());
    }

    @Test
    @DisplayName("Should correctly create a student from CreateStudentCommand")
    void shouldCreateStudentFromCommand() {
        // Arrange
        CreateStudentCommand command = new CreateStudentCommand(
                new PersonName("Luis", "Castillo"),
                new DniNumber("87654321"),
                new EmailAddress("soyluis@outlook.es"),
                Sex.MALE,
                LocalDate.of(2001, 3, 10),
                new StreetAddress("Av. Los Olivos 222", "San Isidro", "Lima", "Lima"),
                new PhoneNumber("+51", "955666333")
        );
        AcademyId academyId = new AcademyId(7L);
        UserId userId = new UserId(2L);

        // Act
        Student student = new Student(command, academyId, userId);

        // Assert
        assertEquals("Luis", student.getPersonName().firstName());
        assertEquals("87654321", student.getDni().dniNumber());
        assertEquals(Sex.MALE, student.getSex());
        assertEquals(LocalDate.of(2001, 3, 10), student.getBirthDate());
        assertEquals("San Isidro", student.getAddress().district());
        assertEquals("955666333", student.getPhoneNumber().phone());
        assertEquals(7L, student.getAcademyId().academyId());
        assertEquals(2L, student.getUserId().userId());
    }

    @Test
    @DisplayName("Should update student information successfully")
    void shouldUpdateStudentInformation() {
        // Arrange
        Student student = new Student(
                new PersonName("Mario", "Lopez"),
                new DniNumber("11223344"),
                Sex.MALE,
                LocalDate.of(1998, 9, 9),
                new StreetAddress("Calle Falsa 123", "SJL", "Lima", "Lima"),
                new PhoneNumber("+51", "912345678"),
                new AcademyId(1L),
                new UserId(1L)
        );

        PersonName updatedName = new PersonName("Mario", "Perez");
        DniNumber updatedDni = new DniNumber("99887766");
        StreetAddress updatedAddress = new StreetAddress("Av. Siempre Viva", "Lince", "Lima", "Lima");
        PhoneNumber updatedPhone = new PhoneNumber("+51", "900111222");

        // Act
        student.updateInformation(updatedName, updatedDni, Sex.MALE, LocalDate.of(1998, 9, 9), updatedAddress, updatedPhone);

        // Assert
        assertEquals("Mario", student.getPersonName().firstName());
        assertEquals("Perez", student.getPersonName().lastName());
        assertEquals("99887766", student.getDni().dniNumber());
        assertEquals("Av. Siempre Viva", student.getAddress().street());
        assertEquals("900111222", student.getPhoneNumber().phone());
    }

    @Test
    @DisplayName("Should throw exception for invalid UserId")
    void shouldThrowExceptionForInvalidUserId() {
        // Arrange, Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new UserId(0L));
        assertEquals("User ID cannot be null or less than or equal to zero", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for invalid DNI format")
    void shouldThrowExceptionForInvalidDni() {
        // Arrange, Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new DniNumber("abc123"));
        assertEquals("DNI number must be exactly 8 characters long", exception.getMessage());
    }
}