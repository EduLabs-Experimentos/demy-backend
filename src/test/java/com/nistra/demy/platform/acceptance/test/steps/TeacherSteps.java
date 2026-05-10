package com.nistra.demy.platform.acceptance.test.steps;

import com.nistra.demy.platform.institution.domain.model.aggregates.Teacher;
import com.nistra.demy.platform.institution.domain.model.commands.RegisterTeacherCommand;
import com.nistra.demy.platform.institution.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PersonName;
import com.nistra.demy.platform.shared.domain.model.valueobjects.PhoneNumber;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TeacherSteps {

    private Long userId;
    private Long academyId;
    private Teacher teacher;
    private Exception exception;
    private String message;

    @Given("un userId {long} asignado por el sistema de usuarios y un academyId {long}")
    public void preparar_ids_de_entorno(Long userId, Long academyId) {
        this.userId = userId;
        this.academyId = academyId;
    }

    @When("intento registrar un profesor con nombre {string}, apellido {string}, correo {string} y celular {string}")
    public void intento_registrar_un_profesor(String nombre, String apellido, String correo, String telefono) {
        try {
            RegisterTeacherCommand command = new RegisterTeacherCommand(
                    new PersonName(nombre, apellido),
                    new EmailAddress(correo),
                    new PhoneNumber("+51", telefono)
            );

            this.teacher = new Teacher(command, new UserId(this.userId), new AcademyId(this.academyId));
            this.message = "Test Passed";
        } catch (Exception e) {
            this.exception = e;
            this.message = "Error";
        }
    }

    @Then("el registro del profesor debe validarse con")
    public void el_registro_del_profesor_debe_validarse_con(DataTable table) {
        if (exception != null) return;

        Map<String, String> rawMap = table.asMap();
        Map<String, String> map = new HashMap<>();
        rawMap.forEach((key, value) -> map.put(key.trim(), value.replaceAll("^\"|\"$", "").trim()));

        assertNotNull(teacher);
        assertEquals(map.get("firstName"), teacher.getPersonName().firstName());
        assertEquals(map.get("lastName"), teacher.getPersonName().lastName());
        assertEquals(map.get("phone"), teacher.getPhoneNumber().phone());
        assertEquals(Long.valueOf(map.get("userId")), teacher.getUserId().userId());
        assertEquals(Long.valueOf(map.get("academyId")), teacher.getAcademyId().academyId());
    }

    @Then("el resultado del registro de profesor es {string}")
    public void el_resultado_del_registro_de_profesor_es(String expectedMessage) {
        assertEquals(expectedMessage, message);
    }
}
