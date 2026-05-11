package com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.Student;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.Sex;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@ActiveProfiles("test")
@DisplayName("StudentRepository Integration Tests")
class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent1;
    private Student testStudent2;
    private Student testStudent3;
    private AcademyId academyId1;
    private AcademyId academyId2;

    @BeforeEach
    void setUp() {
        // Arrange - Preparar datos de prueba comunes
        academyId1 = new AcademyId(1L);
        academyId2 = new AcademyId(2L);

        testStudent1 = new Student(
                new PersonName("Carlos", "Rodriguez"),
                new DniNumber("12345678"),
                Sex.MALE,
                LocalDate.of(2000, 5, 15),
                new StreetAddress("Av. Larco 1234", "Miraflores", "Lima", "Lima"),
                new PhoneNumber("+51", "987654321"),
                academyId1,
                new UserId(100L)
        );

        testStudent2 = new Student(
                new PersonName("Maria", "Gonzales"),
                new DniNumber("87654321"),
                Sex.FEMALE,
                LocalDate.of(1999, 8, 20),
                new StreetAddress("Jr. Union 456", "Cercado", "Lima", "Lima"),
                new PhoneNumber("+51", "912345678"),
                academyId1,
                new UserId(101L)
        );

        testStudent3 = new Student(
                new PersonName("Luis", "Martinez"),
                new DniNumber("11223344"),
                Sex.MALE,
                LocalDate.of(2001, 3, 10),
                new StreetAddress("Av. Arequipa 789", "Lince", "Lima", "Lima"),
                new PhoneNumber("+51", "998877665"),
                academyId2,
                new UserId(102L)
        );
    }

    @Test
    @DisplayName("Should persist and retrieve student with all value objects")
    void save_ValidStudent_PersistsSuccessfully() {
        // Arrange - ya preparado en setUp()

        // Act
        Student savedStudent = studentRepository.save(testStudent1);
        entityManager.flush();
        entityManager.clear();

        Student foundStudent = entityManager.find(Student.class, savedStudent.getId());

        // Assert
        assertNotNull(foundStudent);
        assertEquals("Carlos", foundStudent.getPersonName().firstName());
        assertEquals("Rodriguez", foundStudent.getPersonName().lastName());
        assertEquals("12345678", foundStudent.getDni().dniNumber());
        assertEquals(Sex.MALE, foundStudent.getSex());
        assertEquals(LocalDate.of(2000, 5, 15), foundStudent.getBirthDate());
        assertEquals("Av. Larco 1234", foundStudent.getAddress().street());
        assertEquals("Miraflores", foundStudent.getAddress().district());
        assertEquals("+51", foundStudent.getPhoneNumber().countryCode());
        assertEquals("987654321", foundStudent.getPhoneNumber().phone());
        assertEquals(1L, foundStudent.getAcademyId().academyId());
        assertEquals(100L, foundStudent.getUserId().userId());
    }

    @Test
    @DisplayName("Should return true when student exists by DNI")
    void existsStudentByDni_ExistingDni_ReturnsTrue() {
        // Arrange
        entityManager.persist(testStudent1);
        entityManager.flush();
        DniNumber dniToCheck = new DniNumber("12345678");

        // Act
        boolean exists = studentRepository.existsStudentByDni(dniToCheck);

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when student does not exist by DNI")
    void existsStudentByDni_NonExistingDni_ReturnsFalse() {
        // Arrange
        entityManager.persist(testStudent1);
        entityManager.flush();
        DniNumber nonExistingDni = new DniNumber("99999999");

        // Act
        boolean exists = studentRepository.existsStudentByDni(nonExistingDni);

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should find student by DNI")
    void findByDni_ExistingDni_ReturnsStudent() {
        // Arrange
        entityManager.persist(testStudent1);
        entityManager.flush();
        DniNumber dniToFind = new DniNumber("12345678");

        // Act
        Optional<Student> result = studentRepository.findByDni(dniToFind);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Carlos", result.get().getPersonName().firstName());
        assertEquals("12345678", result.get().getDni().dniNumber());
    }

    @Test
    @DisplayName("Should return empty when student not found by DNI")
    void findByDni_NonExistingDni_ReturnsEmpty() {
        // Arrange
        entityManager.persist(testStudent1);
        entityManager.flush();
        DniNumber nonExistingDni = new DniNumber("99999999");

        // Act
        Optional<Student> result = studentRepository.findByDni(nonExistingDni);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should find all students by academy ID")
    void findAllByAcademyId_ExistingAcademy_ReturnsStudentList() {
        // Arrange
        entityManager.persist(testStudent1);
        entityManager.persist(testStudent2);
        entityManager.persist(testStudent3);
        entityManager.flush();

        // Act
        List<Student> studentsInAcademy1 = studentRepository.findAllByAcademyId(academyId1);

        // Assert
        assertEquals(2, studentsInAcademy1.size());
        assertTrue(studentsInAcademy1.stream()
                .allMatch(s -> s.getAcademyId().academyId().equals(1L)));
        assertTrue(studentsInAcademy1.stream()
                .anyMatch(s -> s.getDni().dniNumber().equals("12345678")));
        assertTrue(studentsInAcademy1.stream()
                .anyMatch(s -> s.getDni().dniNumber().equals("87654321")));
    }

    @Test
    @DisplayName("Should return empty list when no students in academy")
    void findAllByAcademyId_NonExistingAcademy_ReturnsEmptyList() {
        // Arrange
        entityManager.persist(testStudent1);
        entityManager.flush();
        AcademyId nonExistingAcademy = new AcademyId(999L);

        // Act
        List<Student> result = studentRepository.findAllByAcademyId(nonExistingAcademy);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return true when DNI exists in academy excluding student ID")
    void existsByDniAndIdNotAndAcademyId_DuplicateDniDifferentStudent_ReturnsTrue() {
        // Arrange
        entityManager.persist(testStudent1);
        entityManager.persist(testStudent2);
        entityManager.flush();

        DniNumber existingDni = new DniNumber("12345678");
        Long differentStudentId = testStudent2.getId();

        // Act
        boolean exists = studentRepository.existsByDniAndIdNotAndAcademyId(
                existingDni,
                differentStudentId,
                academyId1
        );

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when checking same student's own DNI")
    void existsByDniAndIdNotAndAcademyId_SameStudentDni_ReturnsFalse() {
        // Arrange
        entityManager.persist(testStudent1);
        entityManager.flush();

        DniNumber ownDni = new DniNumber("12345678");
        Long sameStudentId = testStudent1.getId();

        // Act
        boolean exists = studentRepository.existsByDniAndIdNotAndAcademyId(
                ownDni,
                sameStudentId,
                academyId1
        );

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should return false when DNI exists in different academy")
    void existsByDniAndIdNotAndAcademyId_DniInDifferentAcademy_ReturnsFalse() {
        // Arrange
        entityManager.persist(testStudent1);
        entityManager.persist(testStudent3);
        entityManager.flush();

        DniNumber dniFromAcademy1 = new DniNumber("12345678");
        Long studentFromAcademy2Id = testStudent3.getId();

        // Act
        boolean exists = studentRepository.existsByDniAndIdNotAndAcademyId(
                dniFromAcademy1,
                studentFromAcademy2Id,
                academyId2
        );

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should handle student update with new information")
    void updateStudent_ValidChanges_PersistsSuccessfully() {
        // Arrange
        entityManager.persist(testStudent1);
        entityManager.flush();
        entityManager.clear();

        Student studentToUpdate = studentRepository.findById(testStudent1.getId()).orElseThrow();

        // Act
        studentToUpdate.updateInformation(
                new PersonName("CarlosUpdated", "RodriguezUpdated"),
                new DniNumber("12345678"),
                Sex.MALE,
                LocalDate.of(2000, 5, 15),
                new StreetAddress("New Street 999", "San Isidro", "Lima", "Lima"),
                new PhoneNumber("+51", "999888777")
        );

        studentRepository.save(studentToUpdate);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Student updatedStudent = studentRepository.findById(testStudent1.getId()).orElseThrow();
        assertEquals("CarlosUpdated", updatedStudent.getPersonName().firstName());
        assertEquals("RodriguezUpdated", updatedStudent.getPersonName().lastName());
        assertEquals("New Street 999", updatedStudent.getAddress().street());
        assertEquals("San Isidro", updatedStudent.getAddress().district());
        assertEquals("999888777", updatedStudent.getPhoneNumber().phone());
    }

    @Test
    @DisplayName("Should delete student successfully")
    void deleteStudent_ExistingStudent_RemovesFromDatabase() {
        // Arrange
        entityManager.persist(testStudent1);
        entityManager.flush();
        Long studentId = testStudent1.getId();

        // Act
        studentRepository.deleteById(studentId);
        entityManager.flush();

        // Assert
        Optional<Student> deletedStudent = studentRepository.findById(studentId);
        assertFalse(deletedStudent.isPresent());
    }

    @Test
    @DisplayName("Should maintain data integrity with multiple operations")
    void multipleOperations_ConcurrentStudents_MaintainsIntegrity() {
        // Arrange & Act
        entityManager.persist(testStudent1);
        entityManager.persist(testStudent2);
        entityManager.persist(testStudent3);
        entityManager.flush();

        // Assert
        List<Student> allStudents = studentRepository.findAll();
        assertEquals(3, allStudents.size());

        List<Student> academy1Students = studentRepository.findAllByAcademyId(academyId1);
        assertEquals(2, academy1Students.size());

        List<Student> academy2Students = studentRepository.findAllByAcademyId(academyId2);
        assertEquals(1, academy2Students.size());
    }
}