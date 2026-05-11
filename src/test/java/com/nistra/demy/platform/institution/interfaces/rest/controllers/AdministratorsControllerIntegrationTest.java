package com.nistra.demy.platform.institution.interfaces.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nistra.demy.platform.institution.domain.model.aggregates.Administrator;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterAdministratorCommand;
import com.nistra.demy.platform.institution.domain.model.queries.GetAdministratorEmailAddressByUserIdQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetCurrentAdministratorQuery;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.institution.domain.services.AdministratorCommandService;
import com.nistra.demy.platform.institution.domain.services.AdministratorQueryService;
import com.nistra.demy.platform.institution.interfaces.rest.resources.RegisterAdministratorResource;
import com.nistra.demy.platform.shared.application.internal.outboundservices.localization.LocalizationService;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdministratorsController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
                org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class
        })
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class AdministratorsControllerIntegrationTest {

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
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;


    private Administrator sampleAdministrator;
    private EmailAddress sampleEmail;

    @BeforeEach
    void setUp() {
        sampleAdministrator = new Administrator(
                new PersonName("Diego", "Vilca"),
                new PhoneNumber("+51", "999888777"),
                new DniNumber("76543210"),
                new UserId(10L)
        );
        sampleEmail = new EmailAddress("diego.admin@nistra.com");
    }

    @Test
    @DisplayName("POST /administrators con datos válidos retorna 201 Created")
    void registerAdministrator_ValidRequest_Returns201() throws Exception {
        // Arrange
        RegisterAdministratorResource resource = new RegisterAdministratorResource(
                "Diego", "Vilca", "+51", "999888777", "76543210", 10L
        );

        when(administratorCommandService.handle(any(RegisterAdministratorCommand.class)))
                .thenReturn(Optional.of(sampleAdministrator));

        // Act & Assert
        mockMvc.perform(post("/api/v1/administrators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Diego"))
                .andExpect(jsonPath("$.dniNumber").value("76543210"));
    }

    @Test
    @DisplayName("GET /administrators/me retorna 200 y junta datos de admin y de usuario (email)")
    void getCurrentAdministrator_WhenExists_Returns200() throws Exception {
        // Arrange
        when(administratorQueryService.handle(any(GetCurrentAdministratorQuery.class)))
                .thenReturn(Optional.of(sampleAdministrator));

        // Simular que IAM devuelve el email
        when(administratorQueryService.handle(any(GetAdministratorEmailAddressByUserIdQuery.class)))
                .thenReturn(Optional.of(sampleEmail));

        // Act & Assert
        mockMvc.perform(get("/api/v1/administrators/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Diego"))
                .andExpect(jsonPath("$.dniNumber").value("76543210"))
                .andExpect(jsonPath("$.emailAddress").value("diego.admin@nistra.com"));
    }

    @Test
    @DisplayName("GET /administrators/me retorna 404 si el administrador no tiene email asociado")
    void getCurrentAdministrator_WhenNoEmailFound_Returns404() throws Exception {
        // Arrange
        when(administratorQueryService.handle(any(GetCurrentAdministratorQuery.class)))
                .thenReturn(Optional.of(sampleAdministrator));

        // Simular que no hay email
        when(administratorQueryService.handle(any(GetAdministratorEmailAddressByUserIdQuery.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/administrators/me"))
                .andExpect(status().isNotFound());
    }
}