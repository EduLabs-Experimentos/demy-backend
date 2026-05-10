package com.nistra.demy.platform.institution.interfaces.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nistra.demy.platform.institution.domain.model.aggregates.Administrator;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterAdministratorCommand;
import com.nistra.demy.platform.institution.domain.model.queries.GetAdministratorEmailAddressByUserIdQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetCurrentAdministratorQuery;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.institution.domain.services.AdministratorCommandService;
import com.nistra.demy.platform.institution.domain.services.AdministratorQueryService;
import com.nistra.demy.platform.institution.interfaces.rest.resources.AdministratorResource;
import com.nistra.demy.platform.institution.interfaces.rest.resources.RegisterAdministratorResource;
import com.nistra.demy.platform.shared.application.internal.outboundservices.localization.LocalizationService;
import com.nistra.demy.platform.shared.domain.model.valueobjects.*;
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

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdministratorsController.class,
        excludeAutoConfiguration = {
                HibernateJpaAutoConfiguration.class,
                JpaRepositoriesAutoConfiguration.class
        })
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class InstitutionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdministratorCommandService administratorCommandService;

    @MockitoBean
    private AdministratorQueryService administratorQueryService;

    @MockitoBean
    private LocalizationService localizationService;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private static final Long ADMIN_ID = 1L;
    private static final Long ACADEMY_ID = 5L;
    private static final Long USER_ID = 100L;

    private Administrator mockAdministrator;

    @BeforeEach
    void setUp() {
        mockAdministrator = mock(Administrator.class);
        when(mockAdministrator.getId()).thenReturn(ADMIN_ID);
        when(mockAdministrator.getPersonName()).thenReturn(new PersonName("Carlos", "Admin"));
        when(mockAdministrator.getPhoneNumber()).thenReturn(new PhoneNumber("+51", "987654321"));
        when(mockAdministrator.getDniNumber()).thenReturn(new DniNumber("72326006"));
        when(mockAdministrator.getAcademyId()).thenReturn(new AcademyId(ACADEMY_ID));
        when(mockAdministrator.getUserId()).thenReturn(new UserId(USER_ID));
    }

    @Test
    @DisplayName("TI001 — POST /api/v1/administrators con datos válidos retorna 201 Created")
    void registerAdministrator_ValidData_Returns201() throws Exception {
        // Arrange
        RegisterAdministratorResource resource = new RegisterAdministratorResource(
                "Juan", "Admin", "+51", "999888777", "12345678", USER_ID
        );
        when(administratorCommandService.handle(any(RegisterAdministratorCommand.class)))
                .thenReturn(Optional.of(mockAdministrator));

        // Act
        mockMvc.perform(post("/api/v1/administrators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))

        // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Carlos"));
    }

    @Test
    @DisplayName("TI002 — POST /api/v1/administrators cuando servicio retorna vacío retorna 400")
    void registerAdministrator_ServiceReturnsEmpty_Returns400() throws Exception {
        // Arrange
        RegisterAdministratorResource resource = new RegisterAdministratorResource(
                "Juan", "Admin", "+51", "999888777", "12345678", USER_ID
        );
        when(administratorCommandService.handle(any(RegisterAdministratorCommand.class)))
                .thenReturn(Optional.empty());

        // Act
        mockMvc.perform(post("/api/v1/administrators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))

        // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TI003 — GET /api/v1/administrators/me cuando existe retorna 200 con datos del admin")
    void getCurrentAdministrator_WhenExists_Returns200() throws Exception {
        // Arrange
        when(administratorQueryService.handle(any(GetCurrentAdministratorQuery.class)))
                .thenReturn(Optional.of(mockAdministrator));
        when(administratorQueryService.handle(any(GetAdministratorEmailAddressByUserIdQuery.class)))
                .thenReturn(Optional.of(new EmailAddress("carlos@academy.com")));

        // Act
        mockMvc.perform(get("/api/v1/administrators/me"))

        // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Carlos"));
    }

    @Test
    @DisplayName("TI004 — GET /api/v1/administrators/me cuando no existe retorna 404")
    void getCurrentAdministrator_WhenNotExists_Returns404() throws Exception {
        // Arrange
        when(administratorQueryService.handle(any(GetCurrentAdministratorQuery.class)))
                .thenReturn(Optional.empty());

        // Act
        mockMvc.perform(get("/api/v1/administrators/me"))

        // Assert
                .andExpect(status().isNotFound());
    }
}