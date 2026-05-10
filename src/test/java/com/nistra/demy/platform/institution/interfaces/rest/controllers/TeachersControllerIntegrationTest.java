package com.nistra.demy.platform.institution.interfaces.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nistra.demy.platform.institution.domain.model.aggregates.Teacher;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterTeacherCommand;
import com.nistra.demy.platform.institution.domain.model.queries.GetAllTeachersQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetCurrentTeacherQuery;
import com.nistra.demy.platform.institution.domain.model.queries.GetTeacherEmailAddressByUserIdQuery;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.institution.domain.services.TeacherCommandService;
import com.nistra.demy.platform.institution.domain.services.TeacherQueryService;
import com.nistra.demy.platform.institution.interfaces.rest.resources.RegisterTeacherResource;
import com.nistra.demy.platform.shared.application.internal.outboundservices.localization.LocalizationService;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
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

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TeachersController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
                org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class
        })
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class TeachersControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TeacherCommandService teacherCommandService;

    @MockitoBean
    private TeacherQueryService teacherQueryService;

    @MockitoBean
    private LocalizationService localizationService;

    @MockitoBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private Teacher sampleTeacher;
    private EmailAddress sampleEmail;

    @BeforeEach
    void setUp() {
        PersonName personName = new PersonName("Lucia", "Vargas");
        PhoneNumber phoneNumber = new PhoneNumber("+51", "911222333");
        EmailAddress emailAddress = new EmailAddress("lucia.vargas@academy.com");

        RegisterTeacherCommand command = new RegisterTeacherCommand(personName, emailAddress, phoneNumber);

        sampleTeacher = new Teacher(command, new UserId(20L), new AcademyId(5L));
        sampleEmail = emailAddress;
    }

    @Test
    @DisplayName("POST /teachers con datos válidos retorna 201 Created y junta el correo")
    void registerTeacher_ValidRequest_Returns201() throws Exception {
        // Arrange
        RegisterTeacherResource resource = new RegisterTeacherResource(
                "Lucia", "Vargas", "lucia.vargas@academy.com", "+51", "911222333"
        );

        // 1. Mockeamos la creación
        when(teacherCommandService.handle(any(RegisterTeacherCommand.class)))
                .thenReturn(Optional.of(sampleTeacher));
        // 2. Mockeamos la consulta del correo (el Controller lo pide después de crear)
        when(teacherQueryService.handle(any(GetTeacherEmailAddressByUserIdQuery.class)))
                .thenReturn(Optional.of(sampleEmail));

        // Act & Assert
        mockMvc.perform(post("/api/v1/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Lucia"))
                .andExpect(jsonPath("$.emailAddress").value("lucia.vargas@academy.com"))
                .andExpect(jsonPath("$.academyId").value(5L));
    }

    @Test
    @DisplayName("GET /teachers retorna 200 y una lista de profesores con sus correos")
    void getAllTeachers_Returns200AndList() throws Exception {
        // Arrange
        when(teacherQueryService.handle(any(GetAllTeachersQuery.class)))
                .thenReturn(List.of(sampleTeacher));

        when(teacherQueryService.handle(any(GetTeacherEmailAddressByUserIdQuery.class)))
                .thenReturn(Optional.of(sampleEmail));

        // Act & Assert
        mockMvc.perform(get("/api/v1/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // Verificamos que sea un JSON Array de tamaño 1
                .andExpect(jsonPath("$[0].firstName").value("Lucia"))
                .andExpect(jsonPath("$[0].emailAddress").value("lucia.vargas@academy.com"));
    }

    @Test
    @DisplayName("GET /teachers/me retorna 200 y los datos del profesor logueado")
    void getCurrentTeacher_WhenExists_Returns200() throws Exception {
        // Arrange
        when(teacherQueryService.handle(any(GetCurrentTeacherQuery.class)))
                .thenReturn(Optional.of(sampleTeacher));

        when(teacherQueryService.handle(any(GetTeacherEmailAddressByUserIdQuery.class)))
                .thenReturn(Optional.of(sampleEmail));

        // Act & Assert
        mockMvc.perform(get("/api/v1/teachers/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Lucia"))
                .andExpect(jsonPath("$.userId").value(20L))
                .andExpect(jsonPath("$.emailAddress").value("lucia.vargas@academy.com"));
    }
}