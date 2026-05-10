package com.nistra.demy.platform.institution.bdd.steps;

import com.nistra.demy.platform.institution.domain.model.aggregates.Administrator;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterAdministratorCommand;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdministratorSteps {

    private Administrator administrator;
    private Exception exception;
    private String message;

    @When("intento registrar un administrador con nombre {string}, apellido {string}, dni {string}, celular {string} y userId {long}")
    public void intento_registrar_un_administrador(String nombre, String apellido, String dni, String telefono, Long userId) {
        try {
            RegisterAdministratorCommand command = new RegisterAdministratorCommand(
                    new PersonName(nombre, apellido),
                    new PhoneNumber("+51", telefono),
                    new DniNumber(dni),
                    new UserId(userId)
            );

            this.administrator = new Administrator(command);
            this.message = "Test Passed";
        } catch (Exception e) {
            this.exception = e;
            this.message = "Error";
        }
    }

    @Then("el registro del administrador debe validarse con")
    public void el_registro_del_administrador_debe_validarse_con(DataTable table) {
        if (exception != null) return;

        Map<String, String> rawMap = table.asMap();
        Map<String, String> map = new HashMap<>();
        rawMap.forEach((key, value) -> map.put(key.trim(), value.replaceAll("^\"|\"$", "").trim()));

        assertNotNull(administrator);
        assertEquals(map.get("firstName"), administrator.getPersonName().firstName());
        assertEquals(map.get("lastName"), administrator.getPersonName().lastName());
        assertEquals(map.get("dni"), administrator.getDniNumber().dniNumber());
        assertEquals(map.get("phone"), administrator.getPhoneNumber().phone());
        assertEquals(Long.valueOf(map.get("userId")), administrator.getUserId().userId());
    }

    @Then("el resultado del registro de admin es {string}")
    public void el_resultado_del_registro_de_admin_es(String expectedMessage) {
        assertEquals(expectedMessage, message);
    }
}