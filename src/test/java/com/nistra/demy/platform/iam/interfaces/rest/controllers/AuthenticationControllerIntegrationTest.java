package com.nistra.demy.platform.iam.interfaces.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nistra.demy.platform.iam.domain.model.aggregates.User;
import com.nistra.demy.platform.iam.domain.model.commands.SignInCommand;
import com.nistra.demy.platform.iam.domain.model.commands.SignUpCommand;
import com.nistra.demy.platform.iam.domain.model.commands.VerifyUserCommand;
import com.nistra.demy.platform.iam.domain.model.entities.Role;
import com.nistra.demy.platform.iam.domain.model.valueobjects.TenantId;
import com.nistra.demy.platform.iam.domain.services.UserCommandService;
import com.nistra.demy.platform.shared.application.internal.outboundservices.localization.LocalizationService;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import org.apache.commons.lang3.tuple.ImmutablePair;
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
import java.util.Set;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthenticationController.class,
        excludeAutoConfiguration = {
                HibernateJpaAutoConfiguration.class,
                JpaRepositoriesAutoConfiguration.class
        })
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserCommandService userCommandService;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockitoBean
    private LocalizationService localizationService;

    private User mockUser;
    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "user@example.com";
    private static final String TOKEN = "jwt-token-abc123";

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(USER_ID);
        when(mockUser.getEmailAddress()).thenReturn(new EmailAddress(USER_EMAIL));
        when(mockUser.getRoles()).thenReturn(Set.of(Role.getDefaultRole()));
        when(mockUser.getTenantId()).thenReturn(new TenantId(1L));
    }

    // ==================== Sign-In ====================

    @Test
    @DisplayName("Should return 200 OK with token when credentials are valid")
    void signInWithValidCredentialsReturns200() throws Exception {
        // Arrange
        String requestBody = """
                {"emailAddress": "user@example.com", "password": "correctPassword"}""";
        when(userCommandService.handle(any(SignInCommand.class)))
                .thenReturn(Optional.of(ImmutablePair.of(mockUser, TOKEN)));

        // Act & Assert
        mockMvc.perform(post("/api/v1/authentication/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.emailAddress").value(USER_EMAIL))
                .andExpect(jsonPath("$.token").value(TOKEN));
    }

    @Test
    @DisplayName("Should return 404 Not Found when credentials are invalid")
    void signInWithInvalidCredentialsReturns404() throws Exception {
        // Arrange
        String requestBody = """
                {"emailAddress": "user@example.com", "password": "wrongPassword"}""";
        when(userCommandService.handle(any(SignInCommand.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/v1/authentication/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    // ==================== Sign-Up ====================

    @Test
    @DisplayName("Should return 201 Created with user data when sign-up is successful")
    void signUpSuccessfulReturns201() throws Exception {
        // Arrange
        String requestBody = """
                {"emailAddress": "user@example.com", "password": "securePass123", "roles": ["ROLE_USER"]}""";
        when(userCommandService.handle(any(SignUpCommand.class)))
                .thenReturn(Optional.of(mockUser));

        // Act & Assert
        mockMvc.perform(post("/api/v1/authentication/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.emailAddress").value(USER_EMAIL))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"))
                .andExpect(jsonPath("$.tenantId").value(1));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when email is already registered")
    void signUpWithDuplicateEmailReturns400() throws Exception {
        // Arrange
        String requestBody = """
                {"emailAddress": "user@example.com", "password": "securePass123", "roles": ["ROLE_USER"]}""";
        when(userCommandService.handle(any(SignUpCommand.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/v1/authentication/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    // ==================== Verify ====================

    @Test
    @DisplayName("Should return 200 OK with token when verification code is valid")
    void verifyWithValidCodeReturns200() throws Exception {
        // Arrange
        String requestBody = """
                {"email": "user@example.com", "code": "123456"}""";
        when(userCommandService.handle(any(VerifyUserCommand.class)))
                .thenReturn(Optional.of(ImmutablePair.of(mockUser, TOKEN)));

        // Act & Assert
        mockMvc.perform(post("/api/v1/authentication/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.emailAddress").value(USER_EMAIL))
                .andExpect(jsonPath("$.token").value(TOKEN));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when verification code is invalid or expired")
    void verifyWithInvalidCodeReturns400() throws Exception {
        // Arrange
        String requestBody = """
                {"email": "user@example.com", "code": "000000"}""";
        when(userCommandService.handle(any(VerifyUserCommand.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/v1/authentication/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
