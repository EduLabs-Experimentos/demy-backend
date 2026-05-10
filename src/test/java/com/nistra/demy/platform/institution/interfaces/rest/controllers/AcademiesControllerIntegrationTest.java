package com.nistra.demy.platform.institution.interfaces.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nistra.demy.platform.institution.domain.model.aggregates.Academy;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterAcademyCommand;
import com.nistra.demy.platform.institution.domain.model.queries.ExistsAcademyByIdQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetCurrentAcademyQuery;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AcademyDescription;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AcademyName;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AdministratorId;
import com.nistra.demy.platform.institution.domain.model.valueobjects.Ruc;
import com.nistra.demy.platform.institution.domain.services.AcademyCommandService;
import com.nistra.demy.platform.institution.domain.services.AcademyQueryService;
import com.nistra.demy.platform.institution.interfaces.rest.resources.RegisterAcademyResource;
import com.nistra.demy.platform.shared.application.internal.outboundservices.localization.LocalizationService;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StreetAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AcademiesController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
                org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class
        })
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class AcademiesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Usamos @MockitoBean  mockeando los SERVICIOS
    @MockitoBean
    private AcademyCommandService academyCommandService;

    @MockitoBean
    private AcademyQueryService academyQueryService;

    @MockitoBean
    private LocalizationService localizationService;

    // Necesario para que no falle el contexto de JPA al usar @WebMvcTest
    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private Academy sampleAcademy;

    @BeforeEach
    void setUp() {
        // Creamos una academia falsa para que los servicios la devuelvan
        sampleAcademy = new Academy(
                new AcademyName("Nistra Academy"),
                new AcademyDescription("Academia de tecnología"),
                new StreetAddress("Av. Primavera 123", "Surco", "Lima", "Lima"),
                new EmailAddress("contacto@nistra.com"),
                new PhoneNumber("+51", "987654321"),
                new Ruc("10456789123")
        );
        sampleAcademy.assignAdministrator(new AdministratorId(1L));
    }

    @Test
    @DisplayName("POST /academies con datos válidos retorna 201 Created")
    void registerAcademy_ValidRequest_Returns201() throws Exception {
        // Arrange: Preparamos el resource (JSON de entrada)
        RegisterAcademyResource resource = new RegisterAcademyResource(
                "Nistra Academy", "Academia de tecnología", "Av. Primavera 123",
                "Surco", "Lima", "Lima", "contacto@nistra.com",
                "+51", "987654321", "10456789123", 1L
        );

        // Se simula que el servicio hace su trabajo y devuelve la academia
        when(academyCommandService.handle(any(RegisterAcademyCommand.class)))
                .thenReturn(Optional.of(sampleAcademy));

        // Act & Assert
        mockMvc.perform(post("/api/v1/academies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.academyName").value("Nistra Academy"))
                .andExpect(jsonPath("$.ruc").value("10456789123"))
                .andExpect(jsonPath("$.emailAddress").value("contacto@nistra.com"));
    }

    @Test
    @DisplayName("GET /academies/current retorna 200 y los datos de la academia")
    void getCurrentAcademy_WhenExists_Returns200() throws Exception {
        // Arrange
        when(academyQueryService.handle(any(GetCurrentAcademyQuery.class)))
                .thenReturn(Optional.of(sampleAcademy));

        // Act & Assert
        mockMvc.perform(get("/api/v1/academies/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.academyName").value("Nistra Academy"))
                .andExpect(jsonPath("$.emailAddress").value("contacto@nistra.com"));
    }

    @Test
    @DisplayName("GET /academies/current retorna 404 cuando no hay academia asociada")
    void getCurrentAcademy_WhenNotExists_Returns404() throws Exception {
        // Arrange
        when(academyQueryService.handle(any(GetCurrentAcademyQuery.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/academies/current"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("HEAD /academies/{id} retorna 200 si la academia existe")
    void checkAcademyExists_WhenExists_Returns200() throws Exception {
        // Arrange
        Long academyId = 1L;
        when(academyQueryService.handle(any(ExistsAcademyByIdQuery.class)))
                .thenReturn(true);

        // Act & Assert
        mockMvc.perform(head("/api/v1/academies/{id}", academyId))
                .andExpect(status().isOk());
    }
}