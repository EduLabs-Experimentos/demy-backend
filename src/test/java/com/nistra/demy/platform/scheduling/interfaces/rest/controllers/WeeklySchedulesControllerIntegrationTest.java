package com.nistra.demy.platform.scheduling.interfaces.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nistra.demy.platform.enrollment.interfaces.acl.EnrollmentsContextFacade;
import com.nistra.demy.platform.institution.domain.services.TeacherQueryService;
import com.nistra.demy.platform.scheduling.application.internal.outboundservices.acl.ExternalEnrollmentService;
import com.nistra.demy.platform.scheduling.domain.services.ClassroomQueryService;
import com.nistra.demy.platform.scheduling.domain.services.CourseQueryService;
import com.nistra.demy.platform.scheduling.interfaces.rest.transform.ScheduleResourceFromEntityAssembler;
import com.nistra.demy.platform.scheduling.interfaces.rest.transform.WeeklyScheduleResourceFromEntityAssembler;
import com.nistra.demy.platform.shared.application.internal.outboundservices.localization.LocalizationService;
import com.nistra.demy.platform.scheduling.domain.model.aggregates.WeeklySchedule;
import com.nistra.demy.platform.scheduling.domain.model.commands.AddScheduleToWeeklyCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.CreateWeeklyScheduleCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.DeleteWeeklyScheduleCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.RemoveScheduleFromWeeklyCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.UpdateWeeklyScheduleNameCommand;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetAllWeeklySchedulesQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetWeeklyScheduleByIdQuery;
import com.nistra.demy.platform.scheduling.domain.model.valueobjects.DayOfWeek;
import com.nistra.demy.platform.scheduling.domain.services.WeeklyScheduleCommandService;
import com.nistra.demy.platform.scheduling.domain.services.WeeklyScheduleQueryService;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.AddScheduleToWeeklyResource;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.CreateWeeklyScheduleResource;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.UpdateWeeklyScheduleNameResource;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.WeeklyScheduleResource;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WeeklySchedulesController.class,
        excludeAutoConfiguration = {
                HibernateJpaAutoConfiguration.class,
                JpaRepositoriesAutoConfiguration.class
        })
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class WeeklySchedulesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WeeklyScheduleCommandService weeklyScheduleCommandService;

    @MockitoBean
    private WeeklyScheduleQueryService weeklyScheduleQueryService;

    @MockitoBean
    private LocalizationService localizationService;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockitoBean
    private WeeklyScheduleResourceFromEntityAssembler weeklyScheduleResourceFromEntityAssembler;

    @MockitoBean
    private ScheduleResourceFromEntityAssembler scheduleResourceFromEntityAssembler;

    @MockitoBean
    private TeacherQueryService teacherQueryService;

    @MockitoBean
    private CourseQueryService courseQueryService;

    @MockitoBean
    private ClassroomQueryService classroomQueryService;

    @MockitoBean
    private ExternalEnrollmentService externalEnrollmentService;

    @MockitoBean
    private EnrollmentsContextFacade enrollmentsContextFacade;

    private static final Long WEEKLY_SCHEDULE_ID = 1L;
    private static final Long CLASS_SESSION_ID = 50L;
    private static final AcademyId ACADEMY_ID = new AcademyId(1L);

    private WeeklySchedule sampleWeeklySchedule;
    private WeeklyScheduleResource sampleWeeklyScheduleResource;

    @BeforeEach
    void setUp() {
        sampleWeeklySchedule = new WeeklySchedule("Semana 1", ACADEMY_ID);

        when(weeklyScheduleResourceFromEntityAssembler.toResourceFromEntity(any(WeeklySchedule.class)))
                .thenAnswer(inv -> {
                    WeeklySchedule ws = inv.getArgument(0);
                    return new WeeklyScheduleResource(ws.getId(), ws.getName(), List.of());
                });
    }

    @Test
    @DisplayName("TS001 — POST /api/v1/schedules con nombre válido retorna 201 Created")
    void createSchedule_ValidName_Returns201() throws Exception {
        // Arrange
        CreateWeeklyScheduleResource resource = new CreateWeeklyScheduleResource("Semana 1");

        when(weeklyScheduleCommandService.handle(any(CreateWeeklyScheduleCommand.class)))
                .thenReturn(WEEKLY_SCHEDULE_ID);
        when(weeklyScheduleQueryService.handle(any(GetWeeklyScheduleByIdQuery.class)))
                .thenReturn(Optional.of(sampleWeeklySchedule));

        // Act
        mockMvc.perform(post("/api/v1/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))

        // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Semana 1"));
    }

    @Test
    @DisplayName("TS002 — GET /api/v1/schedules/{id} con ID existente retorna 200")
    void getScheduleById_ExistingId_Returns200() throws Exception {
        // Arrange
        when(weeklyScheduleQueryService.handle(any(GetWeeklyScheduleByIdQuery.class)))
                .thenReturn(Optional.of(sampleWeeklySchedule));

        // Act
        mockMvc.perform(get("/api/v1/schedules/{scheduleId}", WEEKLY_SCHEDULE_ID))

        // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Semana 1"));
    }

    @Test
    @DisplayName("TS002 — GET /api/v1/schedules/{id} con ID inexistente retorna 404")
    void getScheduleById_NonExistingId_Returns404() throws Exception {
        // Arrange
        when(weeklyScheduleQueryService.handle(any(GetWeeklyScheduleByIdQuery.class)))
                .thenReturn(Optional.empty());

        // Act
        mockMvc.perform(get("/api/v1/schedules/{scheduleId}", 9999L))

        // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TS003 — GET /api/v1/schedules retorna lista de horarios")
    void getAllSchedules_ReturnsScheduleList() throws Exception {
        // Arrange
        when(weeklyScheduleQueryService.handle(any(GetAllWeeklySchedulesQuery.class)))
                .thenReturn(List.of(sampleWeeklySchedule));

        // Act
        mockMvc.perform(get("/api/v1/schedules"))

        // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Semana 1"));
    }

    @Test
    @DisplayName("TS004 — PUT /api/v1/schedules/{id} con nombre válido retorna 200")
    void updateSchedule_ValidName_Returns200() throws Exception {
        // Arrange
        UpdateWeeklyScheduleNameResource resource = new UpdateWeeklyScheduleNameResource("Semana Renombrada");
        WeeklySchedule updated = new WeeklySchedule("Semana Renombrada", ACADEMY_ID);

        when(weeklyScheduleCommandService.handle(any(UpdateWeeklyScheduleNameCommand.class)))
                .thenReturn(Optional.of(updated));

        // Act
        mockMvc.perform(put("/api/v1/schedules/{scheduleId}", WEEKLY_SCHEDULE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))

        // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Semana Renombrada"));
    }

    @Test
    @DisplayName("TS005 — DELETE /api/v1/schedules/{id} exitoso retorna 200 con mensaje")
    void deleteSchedule_ExistingId_Returns200WithMessage() throws Exception {
        // Arrange
        doNothing().when(weeklyScheduleCommandService).handle(any(DeleteWeeklyScheduleCommand.class));

        // Act
        mockMvc.perform(delete("/api/v1/schedules/{scheduleId}", WEEKLY_SCHEDULE_ID))

        // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Schedule deleted successfully"));
    }

    @Test
    @DisplayName("TS006 — POST /api/v1/schedules/{id}/class-sessions agrega sesión exitosamente")
    void addClassSession_ValidData_Returns200() throws Exception {
        // Arrange
        AddScheduleToWeeklyResource resource = new AddScheduleToWeeklyResource(
                "08:00", "10:00", "MONDAY", 100L, 200L, "Carlos", "Perez"
        );
        WeeklySchedule withSession = new WeeklySchedule("Semana 1", ACADEMY_ID);

        when(weeklyScheduleCommandService.handle(any(AddScheduleToWeeklyCommand.class)))
                .thenReturn(Optional.of(withSession));

        // Act
        mockMvc.perform(post("/api/v1/schedules/{scheduleId}/class-sessions", WEEKLY_SCHEDULE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))

        // Assert
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("TS008 — DELETE /api/v1/schedules/{id}/class-sessions/{classSessionId} elimina sesión exitosamente")
    void removeClassSession_ValidIds_Returns200() throws Exception {
        // Arrange
        WeeklySchedule emptySchedule = new WeeklySchedule("Semana 1", ACADEMY_ID);

        when(weeklyScheduleCommandService.handle(any(RemoveScheduleFromWeeklyCommand.class)))
                .thenReturn(Optional.of(emptySchedule));

        // Act
        mockMvc.perform(delete("/api/v1/schedules/{scheduleId}/class-sessions/{classSessionId}",
                        WEEKLY_SCHEDULE_ID, CLASS_SESSION_ID))

        // Assert
                .andExpect(status().isOk());
    }
}