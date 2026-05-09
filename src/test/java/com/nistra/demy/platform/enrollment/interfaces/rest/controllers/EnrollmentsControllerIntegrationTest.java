package com.nistra.demy.platform.enrollment.interfaces.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nistra.demy.platform.shared.application.internal.outboundservices.localization.LocalizationService;
import com.nistra.demy.platform.enrollment.domain.exceptions.EnrollmentAlreadyExistsException;
import com.nistra.demy.platform.enrollment.domain.exceptions.EnrollmentNotFoundException;
import com.nistra.demy.platform.enrollment.domain.model.aggregates.Enrollment;
import com.nistra.demy.platform.enrollment.domain.model.commands.CreateEnrollmentCommand;
import com.nistra.demy.platform.enrollment.domain.model.commands.DeleteEnrollmentCommand;
import com.nistra.demy.platform.enrollment.domain.model.commands.UpdateEnrollmentCommand;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetAllEnrollmentsQuery;
import com.nistra.demy.platform.enrollment.domain.model.queries.GetEnrollmentByIdQuery;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.*;
import com.nistra.demy.platform.enrollment.domain.services.EnrollmentCommandService;
import com.nistra.demy.platform.enrollment.domain.services.EnrollmentQueryService;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.CreateEnrollmentResource;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.UpdateEnrollmentResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests — 6.1.2 Core Integration Tests
 *
 * Clase bajo prueba: EnrollmentsController
 *
 * Endpoints cubiertos:
 *   POST   /api/v1/enrollments          → TS011 (crear matrícula)
 *   PUT    /api/v1/enrollments/{id}     → TS012 (actualizar matrícula)
 *   DELETE /api/v1/enrollments/{id}     → TS013 (eliminar matrícula)
 *   GET    /api/v1/enrollments          → TS014 (listar matrículas)
 *   GET    /api/v1/enrollments/{id}     → TS015 (detalle de matrícula)
 *
 * User Stories relacionados: US007, US008, US009
 */
@WebMvcTest(controllers = EnrollmentsController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
                org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class
        })
@AutoConfigureMockMvc(addFilters = false)
class EnrollmentsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EnrollmentCommandService enrollmentCommandService;

    @MockitoBean
    private EnrollmentQueryService enrollmentQueryService;


    @MockitoBean
    private LocalizationService localizationService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    // ─────────────────── Fixtures ────────────────────

    private static final Long ENROLLMENT_ID = 1L;
    private static final Long STUDENT_ID    = 10L;
    private static final Long PERIOD_ID     = 20L;
    private static final Long SCHEDULE_ID   = 30L;
    private static final Long ACADEMY_ID    = 5L;

    private Enrollment sampleEnrollment;

    @BeforeEach
    void setUp() {
        sampleEnrollment = Enrollment.createEnrollmentActive(
                new StudentId(STUDENT_ID),
                new PeriodId(PERIOD_ID),
                new ScheduleId(SCHEDULE_ID),
                new AcademyId(ACADEMY_ID),
                new Money(new BigDecimal("500.00"), Currency.getInstance("PEN")),
                PaymentStatus.PENDING
        );
    }

    // ═══════════════════════════════════════════════════
    //   POST /api/v1/enrollments    — TS011 / US007
    // ═══════════════════════════════════════════════════

    @Test
    @DisplayName("TS011 — POST /enrollments con datos válidos retorna 201 Created")
    void createEnrollment_ValidRequest_Returns201() throws Exception {
        // Arrange
        CreateEnrollmentResource resource = new CreateEnrollmentResource(
                STUDENT_ID, PERIOD_ID, SCHEDULE_ID, "500.00", "PEN", "PENDING"
        );
        when(enrollmentCommandService.handle(any(CreateEnrollmentCommand.class)))
                .thenReturn(ENROLLMENT_ID);
        when(enrollmentQueryService.handle(any(GetEnrollmentByIdQuery.class)))
                .thenReturn(Optional.of(sampleEnrollment));

        // Act & Assert
        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(STUDENT_ID))
                .andExpect(jsonPath("$.periodId").value(PERIOD_ID))
                .andExpect(jsonPath("$.enrollmentStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.paymentStatus").value("PENDING"));

    }


    @Test
    @DisplayName("TS011 — POST /enrollments retorna 400 cuando el comando devuelve 0")
    void createEnrollment_InvalidCommand_Returns400() throws Exception {
        // Arrange
        CreateEnrollmentResource resource = new CreateEnrollmentResource(
                STUDENT_ID, PERIOD_ID, SCHEDULE_ID, "500.00", "PEN", "PENDING"
        );
        when(enrollmentCommandService.handle(any(CreateEnrollmentCommand.class)))
                .thenReturn(0L);

        // Act & Assert
        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isBadRequest());
    }

    // ═══════════════════════════════════════════════════
    //   GET /api/v1/enrollments        — TS014
    // ═══════════════════════════════════════════════════

    @Test
    @DisplayName("TS014 — GET /enrollments retorna 200 con lista de matrículas")
    void getAllEnrollments_ExistingRecords_Returns200WithList() throws Exception {
        // Arrange
        when(enrollmentQueryService.handle(any(GetAllEnrollmentsQuery.class)))
                .thenReturn(List.of(sampleEnrollment));

        // Act & Assert
        mockMvc.perform(get("/api/v1/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].studentId").value(STUDENT_ID));
    }

    @Test
    @DisplayName("TS014 — GET /enrollments sin registros retorna 200 con lista vacía")
    void getAllEnrollments_NoRecords_Returns200WithEmptyList() throws Exception {
        // Arrange
        when(enrollmentQueryService.handle(any(GetAllEnrollmentsQuery.class)))
                .thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/v1/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ═══════════════════════════════════════════════════
    //   GET /api/v1/enrollments/{id}   — TS015
    // ═══════════════════════════════════════════════════

    @Test
    @DisplayName("TS015 — GET /enrollments/{id} con ID existente retorna 200")
    void getEnrollmentById_ExistingId_Returns200() throws Exception {
        // Arrange
        when(enrollmentQueryService.handle(any(GetEnrollmentByIdQuery.class)))
                .thenReturn(Optional.of(sampleEnrollment));

        // Act & Assert
        mockMvc.perform(get("/api/v1/enrollments/{id}", ENROLLMENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(STUDENT_ID))
                .andExpect(jsonPath("$.periodId").value(PERIOD_ID))
                .andExpect(jsonPath("$.enrollmentStatus").value("ACTIVE"));
    }

    @Test
    @DisplayName("TS015 — GET /enrollments/{id} con ID inexistente retorna 404")
    void getEnrollmentById_NonExistingId_Returns404() throws Exception {
        // Arrange
        when(enrollmentQueryService.handle(any(GetEnrollmentByIdQuery.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/enrollments/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    // ═══════════════════════════════════════════════════
    //   PUT /api/v1/enrollments/{id}   — TS012 / US008
    // ═══════════════════════════════════════════════════

    @Test
    @DisplayName("TS012 — PUT /enrollments/{id} con datos válidos retorna 200")
    void updateEnrollment_ValidRequest_Returns200() throws Exception {
        // Arrange
        UpdateEnrollmentResource resource = new UpdateEnrollmentResource(
                "600.00", "PEN", "ACTIVE", "PAID"
        );
        Enrollment updated = sampleEnrollment.updateInformation(
                new Money(new BigDecimal("600.00"), Currency.getInstance("PEN")),
                EnrollmentStatus.ACTIVE,
                PaymentStatus.PAID
        );
        when(enrollmentCommandService.handle(any(UpdateEnrollmentCommand.class)))
                .thenReturn(Optional.of(updated));

        // Act & Assert
        mockMvc.perform(put("/api/v1/enrollments/{id}", ENROLLMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentStatus").value("PAID"));
    }



    // ═══════════════════════════════════════════════════
    //   DELETE /api/v1/enrollments/{id}  — TS013 / US009
    // ═══════════════════════════════════════════════════

    @Test
    @DisplayName("TS013 — DELETE /enrollments/{id} exitoso retorna 200 con mensaje")
    void deleteEnrollment_ExistingId_Returns200WithMessage() throws Exception {
        // Arrange
        doNothing().when(enrollmentCommandService).handle(any(DeleteEnrollmentCommand.class));

        // Act & Assert
        mockMvc.perform(delete("/api/v1/enrollments/{id}", ENROLLMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("successfully deleted")));
    }


}
