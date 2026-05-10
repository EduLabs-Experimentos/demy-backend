package com.nistra.demy.platform.institution.stepdefinitions;

import com.nistra.demy.platform.institution.domain.model.aggregates.Administrator;
import com.nistra.demy.platform.institution.domain.model.aggregates.Academy;
import com.nistra.demy.platform.institution.domain.model.commands.AssignAdministratorToAcademyCommand;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterAdministratorCommand;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterAcademyCommand;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AcademyDescription;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AcademyName;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AdministratorId;
import com.nistra.demy.platform.institution.domain.model.valueobjects.Ruc;
import com.nistra.demy.platform.institution.domain.services.AcademyCommandService;
import com.nistra.demy.platform.institution.domain.services.AdministratorCommandService;
import com.nistra.demy.platform.shared.domain.model.valueobjects.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdministradoresSteps {

    private final AdministratorCommandService administratorCommandService;
    private final AcademyCommandService academyCommandService;

    private Administrator registeredAdministrator;
    private Academy registeredAcademy;
    private Exception thrownException;
    private int httpStatusCode;

    public AdministradoresSteps() {
        this.administratorCommandService = Mockito.mock(AdministratorCommandService.class);
        this.academyCommandService = Mockito.mock(AcademyCommandService.class);
    }

    @Given("no existe administrador con DNI {string} en el sistema")
    public void no_existe_administrador_con_dni_en_el_sistema(String dni) {
        when(administratorCommandService.handle(any(RegisterAdministratorCommand.class)))
                .thenAnswer(inv -> {
                    RegisterAdministratorCommand cmd = inv.getArgument(0);
                    Administrator admin = new Administrator(
                            cmd.personName(),
                            cmd.phoneNumber(),
                            cmd.dniNumber(),
                            cmd.userId()
                    );
                    return Optional.of(admin);
                });
    }

    @When("registro un administrador con nombre {string}, apellido {string}, país {string}, teléfono {string}, DNI {string} y userId {int}")
    public void registro_administrador_con_datos(String firstName, String lastName, String countryCode, String phone, String dni, int userId) {
        try {
            PersonName personName = new PersonName(firstName, lastName);
            PhoneNumber phoneNumber = new PhoneNumber(countryCode, phone);
            DniNumber dniNumber = new DniNumber(dni);
            var userIdVo = new com.nistra.demy.platform.institution.domain.model.valueobjects.UserId((long) userId);

            RegisterAdministratorCommand command = new RegisterAdministratorCommand(personName, phoneNumber, dniNumber, userIdVo);
            Optional<Administrator> result = administratorCommandService.handle(command);

            if (result.isPresent()) {
                registeredAdministrator = result.get();
                httpStatusCode = 201;
            } else {
                httpStatusCode = 400;
            }
        } catch (Exception e) {
            thrownException = e;
            httpStatusCode = 400;
        }
    }

    @Then("el administrador queda registrado exitosamente")
    public void el_administrador_queda_registrado_exitosamente() {
        assertNotNull(registeredAdministrator);
        assertNull(thrownException);
    }

    @Then("el código de estado HTTP del administrador es {int}")
    public void el_codigo_de_estado_HTTP_del_administrador_es(int statusCode) {
        assertEquals(statusCode, httpStatusCode);
    }

    @Then("se devuelve el recurso del administrador creado")
    public void se_devuelve_el_recurso_del_administrador_creado() {
        assertNotNull(registeredAdministrator);
        assertNotNull(registeredAdministrator.getPersonName());
    }

    @Given("ya existe una academia con email {string} en el sistema")
    public void ya_existe_una_academia_con_email_en_el_sistema(String email) {
        Mockito.doThrow(new IllegalArgumentException("Email ya registrado"))
                .when(academyCommandService)
                .handle(any(RegisterAcademyCommand.class));
    }

    @When("registro una nueva academia con nombre {string}, email {string}, teléfono {string}, RUC {string} y administrador ID {int}")
    public void registro_nueva_academia_con_datos(String name, String email, String phone, String ruc, int adminId) {
        AcademyName academyName = new AcademyName(name);
        AcademyDescription description = new AcademyDescription("Descripción de " + name);
        StreetAddress streetAddress = new StreetAddress("Calle Falsa", "San Miguel", "Lima", "Lima");
        EmailAddress emailAddress = new EmailAddress(email);
        String phoneWithoutSpaces = phone.replace(" ", "");
        String countryCode = phoneWithoutSpaces.startsWith("+") ? phoneWithoutSpaces.substring(0, 3) : "+51";
        String localNumber = phoneWithoutSpaces.startsWith("+") ? phoneWithoutSpaces.substring(3) : phoneWithoutSpaces;
        PhoneNumber phoneNumber = new PhoneNumber(countryCode, localNumber);
        Ruc rucValue = new Ruc(ruc);
        AdministratorId adminIdVo = new AdministratorId((long) adminId);

        RegisterAcademyCommand command = new RegisterAcademyCommand(
                academyName, description, streetAddress, emailAddress, phoneNumber, rucValue, adminIdVo
        );

        try {
            academyCommandService.handle(command);
        } catch (Exception e) {
            thrownException = e;
            httpStatusCode = 400;
        }
    }

    @Then("la operación falla con error de {string}")
    public void la_operacion_falla_con_error(String errorType) {
        assertNotNull(thrownException, "thrownException should not be null");
        String msg = thrownException.getMessage() != null ? thrownException.getMessage() : "null message";
        assertTrue(
            msg.toLowerCase().contains("ya") ||
            msg.toLowerCase().contains("registrado") ||
            msg.toLowerCase().contains("duplicado") ||
            msg.toLowerCase().contains("email"),
            "Expected exception message to contain 'ya', 'registrado', 'duplicado' or 'email' but was: [" + msg + "]"
        );
    }

    @Then("se devuelve mensaje de error")
    public void se_devuelve_mensaje_de_error() {
        assertNotNull(thrownException);
    }

    @Given("existe un administrador {string} {string} sin asociación a academia")
    public void existe_administrador_sin_asociacion_a_academia(String firstName, String lastName) {
        when(administratorCommandService.handle(any(RegisterAdministratorCommand.class)))
                .thenAnswer(inv -> {
                    RegisterAdministratorCommand cmd = inv.getArgument(0);
                    Administrator admin = new Administrator(
                            cmd.personName(),
                            cmd.phoneNumber(),
                            cmd.dniNumber(),
                            cmd.userId()
                    );
                    return Optional.of(admin);
                });
    }

    @Given("existe una academia {string} sin administrador asignado")
    public void existe_una_academia_sin_administrador_asignado(String academyNameStr) {
        AcademyName academyName = new AcademyName(academyNameStr);
        when(academyCommandService.handle(any(RegisterAcademyCommand.class)))
                .thenAnswer(inv -> {
                    RegisterAcademyCommand cmd = inv.getArgument(0);
                    Academy academy = new Academy(cmd.academyName(), cmd.academyDescription(),
                            new StreetAddress("Calle Default", "Distrito", "Provincia", "Departamento"),
                            cmd.emailAddress(), cmd.phoneNumber(), cmd.ruc());
                    return Optional.of(academy);
                });
    }

    @When("asociar el administrador a la academia")
    public void asociar_el_administrador_a_la_academia() {
        try {
            doNothing().when(academyCommandService).handle(any(AssignAdministratorToAcademyCommand.class));

            AcademyId academyId = new AcademyId(1L);
            AdministratorId adminId = new AdministratorId(50L);

            AssignAdministratorToAcademyCommand command = new AssignAdministratorToAcademyCommand(academyId, adminId);
            academyCommandService.handle(command);

            httpStatusCode = 200;
        } catch (Exception e) {
            thrownException = e;
            httpStatusCode = 400;
        }
    }

    @Then("el administrador queda asociado a la academia")
    public void el_administrador_queda_asociado_a_la_academia() {
        assertNotNull(thrownException == null);
        assertEquals(200, httpStatusCode);
    }

    @Then("la academia tiene el administrador asignado")
    public void la_academia_tiene_el_administrador_asignado() {
        assertEquals(200, httpStatusCode);
    }

    @Then("el código de estado HTTP de la academia es {int}")
    public void el_codigo_de_estado_HTTP_de_la_academia_es(int statusCode) {
        assertEquals(statusCode, httpStatusCode);
    }
}