package com.nistra.demy.platform.enrollment.application.internal.commandservices;

import com.nistra.demy.platform.enrollment.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.enrollment.application.internal.outboundservices.acl.ExternalSchedulingService;
import com.nistra.demy.platform.enrollment.domain.exceptions.EnrollmentAlreadyExistsException;
import com.nistra.demy.platform.enrollment.domain.exceptions.EnrollmentNotFoundException;
import com.nistra.demy.platform.enrollment.domain.model.aggregates.Enrollment;
import com.nistra.demy.platform.enrollment.domain.model.aggregates.Student;
import com.nistra.demy.platform.enrollment.domain.model.commands.CreateEnrollmentCommand;
import com.nistra.demy.platform.enrollment.domain.model.commands.DeleteEnrollmentCommand;
import com.nistra.demy.platform.enrollment.domain.model.commands.UpdateEnrollmentCommand;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.*;
import com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories.EnrollmentRepository;
import com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories.StudentRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests — 6.1.1 Core Entities Unit Tests
 *
 * Clase bajo prueba: EnrollmentCommandServiceImpl
 *
 * Comportamientos cubiertos:
 *   - handle(CreateEnrollmentCommand): creación exitosa, duplicado, academia no encontrada,
 *     horario no encontrado, estudiante no encontrado, monto inválido.
 *   - handle(DeleteEnrollmentCommand): eliminación exitosa, matrícula no encontrada,
 *     academia diferente.
 *   - handle(UpdateEnrollmentCommand): actualización exitosa, matrícula no encontrada,
 *     academia diferente.
 *
 * User Stories relacionados: US007, US008, US009
 * Technical Stories relacionados: TS011, TS012, TS013
 */

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EnrollmentCommandServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ExternalSchedulingService externalSchedulingService;

    @Mock
    private ExternalIamService externalIamService;

    @InjectMocks
    private EnrollmentCommandServiceImpl enrollmentCommandService;


    private static final Long   STUDENT_ID  = 1L;
    private static final Long   PERIOD_ID   = 10L;
    private static final Long   SCHEDULE_ID = 100L;
    private static final Long   ACADEMY_ID  = 5L;
    private static final Long   ENROLLMENT_ID = 999L;

    private StudentId  studentId;
    private PeriodId   periodId;
    private ScheduleId scheduleId;
    private AcademyId  academyId;
    private Money      money;

    private CreateEnrollmentCommand createCommand;
    private UpdateEnrollmentCommand updateCommand;
    private DeleteEnrollmentCommand deleteCommand;

    private Enrollment enrollment;
    private Student    student;

    @BeforeEach
    void setUp() {
        studentId  = new StudentId(STUDENT_ID);
        periodId   = new PeriodId(PERIOD_ID);
        scheduleId = new ScheduleId(SCHEDULE_ID);
        academyId  = new AcademyId(ACADEMY_ID);
        money      = new Money(new BigDecimal("500.00"), Currency.getInstance("PEN"));

        createCommand = new CreateEnrollmentCommand(
                studentId, periodId, scheduleId, money, PaymentStatus.PENDING
        );

        updateCommand = new UpdateEnrollmentCommand(
                ENROLLMENT_ID,
                new Money(new BigDecimal("600.00"), Currency.getInstance("PEN")),
                EnrollmentStatus.ACTIVE,
                PaymentStatus.PAID
        );

        deleteCommand = new DeleteEnrollmentCommand(ENROLLMENT_ID);

        // Enrollment creado via factory (ACTIVE, monto válido)
        enrollment = Enrollment.createEnrollmentActive(
                studentId, periodId, scheduleId, academyId, money, PaymentStatus.PENDING
        );

        student = mock(Student.class);
        when(student.getDni()).thenReturn(new DniNumber("12345678"));
    }

    // ═══════════════════════════════════════════════════
    //   handle(CreateEnrollmentCommand)  — US007 / TS011
    // ═══════════════════════════════════════════════════

    @Test
    @DisplayName("US007 — Crear matrícula exitosamente retorna el ID generado")
    void handle_CreateEnrollment_Success_ReturnsId() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(externalSchedulingService.fetchScheduleById(SCHEDULE_ID)).thenReturn(Optional.of(scheduleId));
        when(enrollmentRepository.findByStudentIdAndPeriodId(studentId, periodId)).thenReturn(Optional.empty());
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(inv -> {
            Enrollment e = inv.getArgument(0);
            return e;
        });

        // Act
        Long result = enrollmentCommandService.handle(createCommand);

        // Assert
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("US007 — Crear matrícula duplicada lanza EnrollmentAlreadyExistsException")
    void handle_CreateEnrollment_DuplicateEnrollment_ThrowsAlreadyExistsException() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(externalSchedulingService.fetchScheduleById(SCHEDULE_ID)).thenReturn(Optional.of(scheduleId));
        when(enrollmentRepository.findByStudentIdAndPeriodId(studentId, periodId))
                .thenReturn(Optional.of(enrollment));

        // Act & Assert
        assertThatThrownBy(() -> enrollmentCommandService.handle(createCommand))
                .isInstanceOf(EnrollmentAlreadyExistsException.class);

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("US007 — Academia no encontrada lanza IllegalArgumentException")
    void handle_CreateEnrollment_AcademyNotFound_ThrowsIllegalArgumentException() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> enrollmentCommandService.handle(createCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Academy not found");
    }

    @Test
    @DisplayName("US007 — Horario no encontrado lanza IllegalArgumentException")
    void handle_CreateEnrollment_ScheduleNotFound_ThrowsIllegalArgumentException() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(externalSchedulingService.fetchScheduleById(SCHEDULE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> enrollmentCommandService.handle(createCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Schedule not found");
    }

    @Test
    @DisplayName("US007 — Estudiante no encontrado lanza IllegalArgumentException")
    void handle_CreateEnrollment_StudentNotFound_ThrowsIllegalArgumentException() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(externalSchedulingService.fetchScheduleById(SCHEDULE_ID)).thenReturn(Optional.of(scheduleId));
        when(enrollmentRepository.findByStudentIdAndPeriodId(studentId, periodId)).thenReturn(Optional.empty());
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> enrollmentCommandService.handle(createCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Student not found");
    }

    @Test
    @DisplayName("US007 — Monto cero o negativo en Enrollment.createEnrollmentActive lanza IllegalArgumentException")
    void createEnrollmentActive_InvalidAmount_ThrowsIllegalArgumentException() {
        // Arrange
        Money invalidMoney = new Money(BigDecimal.ZERO, Currency.getInstance("PEN"));

        // Act & Assert
        assertThatThrownBy(() ->
                Enrollment.createEnrollmentActive(studentId, periodId, scheduleId, academyId, invalidMoney, PaymentStatus.PENDING)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("amount must be positive");
    }

    // ═══════════════════════════════════════════════════
    //   handle(DeleteEnrollmentCommand)  — US009 / TS013
    // ═══════════════════════════════════════════════════

    @Test
    @DisplayName("US009 — Eliminar matrícula existente en academia correcta llama deleteById")
    void handle_DeleteEnrollment_Success_CallsDeleteById() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(enrollmentRepository.findById(ENROLLMENT_ID)).thenReturn(Optional.of(enrollment));

        // Act
        enrollmentCommandService.handle(deleteCommand);

        // Assert
        verify(enrollmentRepository, times(1)).deleteById(ENROLLMENT_ID);
    }

    @Test
    @DisplayName("US009 — Eliminar matrícula inexistente lanza EnrollmentNotFoundException")
    void handle_DeleteEnrollment_NotFound_ThrowsEnrollmentNotFoundException() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(enrollmentRepository.findById(ENROLLMENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> enrollmentCommandService.handle(deleteCommand))
                .isInstanceOf(EnrollmentNotFoundException.class);

        verify(enrollmentRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("US009 — Eliminar matrícula de academia diferente lanza EnrollmentNotFoundException")
    void handle_DeleteEnrollment_DifferentAcademy_ThrowsEnrollmentNotFoundException() {
        // Arrange
        AcademyId otherAcademy = new AcademyId(99L);
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(otherAcademy));
        when(enrollmentRepository.findById(ENROLLMENT_ID)).thenReturn(Optional.of(enrollment));

        // Act & Assert
        assertThatThrownBy(() -> enrollmentCommandService.handle(deleteCommand))
                .isInstanceOf(EnrollmentNotFoundException.class);
    }

    @Test
    @DisplayName("US009 — Academia no encontrada al eliminar lanza IllegalArgumentException")
    void handle_DeleteEnrollment_AcademyNotFound_ThrowsIllegalArgumentException() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> enrollmentCommandService.handle(deleteCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Academy context not found");
    }

    // ═══════════════════════════════════════════════════
    //   handle(UpdateEnrollmentCommand)  — US008 / TS012
    // ═══════════════════════════════════════════════════

    @Test
    @DisplayName("US008 — Actualizar matrícula existente retorna Optional con matrícula actualizada")
    void handle_UpdateEnrollment_Success_ReturnsUpdatedEnrollment() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(enrollmentRepository.findById(ENROLLMENT_ID)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Optional<Enrollment> result = enrollmentCommandService.handle(updateCommand);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEnrollmentStatus()).isEqualTo(EnrollmentStatus.ACTIVE);
        assertThat(result.get().getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    @DisplayName("US008 — Actualizar matrícula inexistente lanza EnrollmentNotFoundException")
    void handle_UpdateEnrollment_NotFound_ThrowsEnrollmentNotFoundException() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(academyId));
        when(enrollmentRepository.findById(ENROLLMENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> enrollmentCommandService.handle(updateCommand))
                .isInstanceOf(EnrollmentNotFoundException.class);
    }

    @Test
    @DisplayName("US008 — Actualizar matrícula de academia diferente lanza EnrollmentNotFoundException")
    void handle_UpdateEnrollment_DifferentAcademy_ThrowsEnrollmentNotFoundException() {
        // Arrange
        AcademyId otherAcademy = new AcademyId(99L);
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.of(otherAcademy));
        when(enrollmentRepository.findById(ENROLLMENT_ID)).thenReturn(Optional.of(enrollment));

        // Act & Assert
        assertThatThrownBy(() -> enrollmentCommandService.handle(updateCommand))
                .isInstanceOf(EnrollmentNotFoundException.class);
    }

    @Test
    @DisplayName("US008 — Academia no encontrada al actualizar lanza IllegalArgumentException")
    void handle_UpdateEnrollment_AcademyNotFound_ThrowsIllegalArgumentException() {
        // Arrange
        when(externalIamService.fetchCurrentAcademyId()).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> enrollmentCommandService.handle(updateCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Academy context not found");
    }

    // ═══════════════════════════════════════════════════
    //   Enrollment aggregate
    // ═══════════════════════════════════════════════════

    @Test
    @DisplayName("Enrollment.createEnrollmentActive — estado inicial siempre es ACTIVE")
    void createEnrollmentActive_ValidData_StatusIsActive() {
        Enrollment e = Enrollment.createEnrollmentActive(
                studentId, periodId, scheduleId, academyId, money, PaymentStatus.PENDING
        );

        assertThat(e.getEnrollmentStatus()).isEqualTo(EnrollmentStatus.ACTIVE);
        assertThat(e.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(e.getStudentId()).isEqualTo(studentId);
        assertThat(e.getPeriodId()).isEqualTo(periodId);
    }

    @Test
    @DisplayName("Enrollment.updateInformation — actualiza money, status y paymentStatus")
    void updateInformation_ValidData_FieldsAreUpdated() {
        Money newMoney = new Money(new BigDecimal("750.00"), Currency.getInstance("PEN"));

        Enrollment updated = enrollment.updateInformation(newMoney, EnrollmentStatus.WITHDRAWN, PaymentStatus.REFUNDED);

        assertThat(updated.getMoney()).isEqualTo(newMoney);
        assertThat(updated.getEnrollmentStatus()).isEqualTo(EnrollmentStatus.WITHDRAWN);
        assertThat(updated.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
    }

    // ═══════════════════════════════════════════════════
    //   Commands
    // ═══════════════════════════════════════════════════

    @Test
    @DisplayName("CreateEnrollmentCommand — studentId null lanza IllegalArgumentException")
    void createEnrollmentCommand_NullStudentId_ThrowsException() {
        assertThatThrownBy(() ->
                new CreateEnrollmentCommand(null, periodId, scheduleId, money, PaymentStatus.PENDING)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("StudentId cannot be null");
    }

    @Test
    @DisplayName("CreateEnrollmentCommand — periodId null lanza IllegalArgumentException")
    void createEnrollmentCommand_NullPeriodId_ThrowsException() {
        assertThatThrownBy(() ->
                new CreateEnrollmentCommand(studentId, null, scheduleId, money, PaymentStatus.PENDING)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessageContaining("PeriodId cannot be null");
    }

    @Test
    @DisplayName("UpdateEnrollmentCommand — enrollmentId nulo o menor a 1 lanza IllegalArgumentException")
    void updateEnrollmentCommand_InvalidEnrollmentId_ThrowsException() {
        assertThatThrownBy(() ->
                new UpdateEnrollmentCommand(0L, money, EnrollmentStatus.ACTIVE, PaymentStatus.PAID)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("UpdateEnrollmentCommand — money negativa lanza IllegalArgumentException")
    void updateEnrollmentCommand_NegativeMoney_ThrowsException() {
        assertThatThrownBy(() ->
                new Money(new BigDecimal("-1.00"), Currency.getInstance("PEN"))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
