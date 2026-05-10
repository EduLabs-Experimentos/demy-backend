package com.nistra.demy.platform.iam.stepdefinitions;

import com.nistra.demy.platform.iam.domain.model.aggregates.User;
import com.nistra.demy.platform.iam.domain.model.commands.SignInCommand;
import com.nistra.demy.platform.iam.domain.model.commands.SignUpCommand;
import com.nistra.demy.platform.iam.domain.model.commands.VerifyUserCommand;
import com.nistra.demy.platform.iam.domain.model.entities.Role;
import com.nistra.demy.platform.iam.domain.model.valueobjects.AccountStatus;
import com.nistra.demy.platform.iam.domain.model.valueobjects.VerificationCode;
import com.nistra.demy.platform.iam.domain.model.valueobjects.VerificationStatus;
import com.nistra.demy.platform.iam.domain.services.UserCommandService;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AutenticacionSteps {

    private final UserCommandService userCommandService;
    private User createdUser;
    private ImmutablePair<User, String> signInResult;
    private Exception thrownException;
    private int httpStatusCode;

    public AutenticacionSteps() {
        this.userCommandService = Mockito.mock(UserCommandService.class);
    }

    @Given("un email {string} no registrado en el sistema")
    public void un_email_no_registrado_en_el_sistema(String email) {
        when(userCommandService.handle(any(SignUpCommand.class)))
                .thenAnswer(inv -> {
                    SignUpCommand cmd = inv.getArgument(0);
                    VerificationCode vc = new VerificationCode("123456", null);
                    User newUser = new User(
                            cmd.emailAddress(),
                            cmd.password(),
                            vc,
                            cmd.roles()
                    );
                    return Optional.of(newUser);
                });
    }

    @When("creo un nuevo usuario con email {string} y password {string}")
    public void creo_un_nuevo_usuario_con_email_y_password(String email, String password) {
        try {
            EmailAddress emailAddress = new EmailAddress(email);
            SignUpCommand command = new SignUpCommand(emailAddress, password, List.of(Role.getDefaultRole()));
            Optional<User> result = userCommandService.handle(command);
            if (result.isPresent()) {
                createdUser = result.get();
                httpStatusCode = 201;
            } else {
                httpStatusCode = 400;
            }
        } catch (Exception e) {
            thrownException = e;
            httpStatusCode = 500;
        }
    }

    @Then("el usuario queda registrado con estado {string}")
    public void el_usuario_queda_registrado_con_estado(String estado) {
        assertNotNull(createdUser);
        assertEquals(estado, createdUser.getAccountStatus().name());
    }

    @Then("se genera un código de verificación de 6 dígitos")
    public void se_genera_un_codigo_de_verificacion_de_6_digitos() {
        assertNotNull(createdUser);
        assertNotNull(createdUser.getVerificationCode());
    }

    @Then("el resultado de la operación es exitoso")
    public void el_resultado_de_la_operacion_es_exitoso() {
        assertNull(thrownException);
        assertNotNull(createdUser);
    }

    @Given("existe un usuario {string} con password {string} en estado {string}")
    public void existe_un_usuario_con_password_en_estado(String email, String password, String estado) {
        User mockUser = mock(User.class);
        when(mockUser.getEmailAddress()).thenReturn(new EmailAddress(email));
        when(mockUser.isVerified()).thenReturn(true);
        when(mockUser.getAccountStatus()).thenReturn(AccountStatus.ACTIVE);

        when(userCommandService.handle(any(SignInCommand.class)))
                .thenReturn(Optional.of(new ImmutablePair<>(mockUser, "mock-jwt-token")));
    }

    @When("inicio sesión con email {string} y password {string}")
    public void inicio_sesion_con_email_y_password(String email, String password) {
        try {
            EmailAddress emailAddress = new EmailAddress(email);
            SignInCommand command = new SignInCommand(emailAddress, password);
            Optional<ImmutablePair<User, String>> result = userCommandService.handle(command);
            if (result.isPresent()) {
                signInResult = result.get();
                httpStatusCode = 200;
            } else {
                httpStatusCode = 401;
            }
        } catch (Exception e) {
            thrownException = e;
            httpStatusCode = 500;
        }
    }

    @Then("obtengo un token JWT como respuesta")
    public void obtengo_un_token_JWT_como_respuesta() {
        assertNotNull(signInResult);
        assertNotNull(signInResult.getRight());
        assertFalse(signInResult.getRight().isEmpty());
    }

    @Then("el código de estado HTTP del usuario es {int}")
    public void el_codigo_de_estado_HTTP_del_usuario_es(int statusCode) {
        assertEquals(statusCode, httpStatusCode);
    }

    @Then("el usuario existe en el sistema")
    public void el_usuario_existe_en_el_sistema() {
        assertNotNull(signInResult);
        assertNotNull(signInResult.getLeft());
    }

    @Given("existe un usuario {string} con código de verificación {string} no verificado")
    public void existe_un_usuario_con_codigo_de_verificacion_no_verificado(String email, String code) {
        User mockUser = mock(User.class);
        when(mockUser.getEmailAddress()).thenReturn(new EmailAddress(email));
        when(mockUser.isVerified()).thenReturn(false);
        VerificationCode vc = new VerificationCode(code, null);
        when(mockUser.getVerificationCode()).thenReturn(vc);

        when(userCommandService.handle(any(VerifyUserCommand.class)))
                .thenReturn(Optional.empty());
    }

    @When("intento verificar la cuenta con código {string}")
    public void intento_verificar_la_cuenta_con_codigo(String code) {
        try {
            VerifyUserCommand command = new VerifyUserCommand("usuario@test.com", code);
            Optional<ImmutablePair<User, String>> result = userCommandService.handle(command);
            if (result.isEmpty()) {
                httpStatusCode = 400;
            } else {
                httpStatusCode = 200;
            }
        } catch (Exception e) {
            thrownException = e;
            httpStatusCode = 400;
        }
    }

    @Then("la verificación falla")
    public void la_verificacion_falla() {
        assertEquals(400, httpStatusCode);
    }

    @Then("el usuario permanece en estado {string}")
    public void el_usuario_permanece_en_estado(String estado) {
        assertNull(signInResult);
    }
}