package com.nistra.demy.platform.institution.bdd.steps;

import com.nistra.demy.platform.institution.domain.model.aggregates.Academy;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterAcademyCommand;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AcademyDescription;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AcademyName;
import com.nistra.demy.platform.institution.domain.model.valueobjects.AdministratorId;
import com.nistra.demy.platform.institution.domain.model.valueobjects.Ruc;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.StreetAddress;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AcademySteps {

    private Long adminId;
    private Academy academy;
    private Exception exception;
    private String message;

    @Given("un administrador con id {long}")
    public void un_administrador_con_id(Long id) {
        this.adminId = id;
    }

    @When("intento registrar una academia con nombre {string}, ruc {string} y correo {string}")
    public void intento_registrar_una_academia(String nombre, String ruc, String email) {
        try {
            // Simulamos la entrada de datos construyendo los Value Objects y el Command
            RegisterAcademyCommand command = new RegisterAcademyCommand(
                    new AcademyName(nombre),
                    new AcademyDescription("Descripción por defecto"),
                    new StreetAddress("Av. Falsa 123", "Distrito", "Provincia", "Departamento"),
                    new EmailAddress(email),
                    new PhoneNumber("+51", "999888777"),
                    new Ruc(ruc),
                    new AdministratorId(this.adminId)
            );

            // 1. Creamos la academia
            this.academy = new Academy(command);

            // 2. Simulamos el paso del CommandService asignando el administrador
            if (this.adminId != null) {
                this.academy.assignAdministrator(new AdministratorId(this.adminId));
            }

            this.message = "Test Passed";
        } catch (Exception e) {
            this.exception = e;
            this.message = "Error";
        }
    }

    @Then("el registro de la academia debe validarse con")
    public void el_registro_de_la_academia_debe_validarse_con(DataTable table) {
        if (exception != null) {
            return;
        }

        Map<String, String> rawMap = table.asMap();
        Map<String, String> map = new HashMap<>();
        rawMap.forEach((key, value) -> {
            map.put(key.trim(), value.replaceAll("^\"|\"$", "").trim());
        });

        // Verificamos que la academia se construyó con los datos exactos del escenario
        assertNotNull(academy);
        assertEquals(map.get("academyName"), academy.getAcademyName().name());
        assertEquals(map.get("ruc"), academy.getRuc().ruc());
        assertEquals(map.get("emailAddress"), academy.getEmailAddress().email());
        assertEquals(this.adminId, academy.getAdministratorId().administratorId());
    }

    @Then("el resultado de la creacion es {string}")
    public void el_resultado_de_la_creacion_es(String expectedMessage) {
        assertEquals(expectedMessage, message);
    }
}
