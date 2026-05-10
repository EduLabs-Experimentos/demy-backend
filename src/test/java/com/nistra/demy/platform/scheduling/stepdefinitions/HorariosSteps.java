package com.nistra.demy.platform.scheduling.stepdefinitions;

import com.nistra.demy.platform.scheduling.domain.model.aggregates.WeeklySchedule;
import com.nistra.demy.platform.scheduling.domain.model.commands.AddScheduleToWeeklyCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.CreateWeeklyScheduleCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.DeleteWeeklyScheduleCommand;
import com.nistra.demy.platform.scheduling.domain.model.entities.Schedule;
import com.nistra.demy.platform.scheduling.domain.services.WeeklyScheduleCommandService;
import com.nistra.demy.platform.scheduling.domain.services.WeeklyScheduleQueryService;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HorariosSteps {

    private final WeeklyScheduleCommandService commandService;
    private final WeeklyScheduleQueryService queryService;

    private WeeklySchedule createdSchedule;
    private WeeklySchedule scheduleWithSession;
    private Long returnedId;
    private Exception thrownException;
    private int httpStatusCode;
    private String responseMessage;

    public HorariosSteps() {
        this.commandService = Mockito.mock(WeeklyScheduleCommandService.class);
        this.queryService = Mockito.mock(WeeklyScheduleQueryService.class);
    }

    @Given("no existe un schedule con nombre {string} para la academia con ID {int}")
    public void no_existe_un_schedule_con_nombre_para_la_academia(String name, int academyId) {
        when(commandService.handle(any(CreateWeeklyScheduleCommand.class)))
                .thenReturn(1L);
    }

    @When("creo un horario semanal con nombre {string} para la academia con ID {int}")
    public void creo_un_horario_semanal_con_nombre_para_la_academia(String name, int academyId) {
        try {
            CreateWeeklyScheduleCommand command = new CreateWeeklyScheduleCommand(name);
            Long id = commandService.handle(command);
            if (id != null && id > 0) {
                returnedId = id;
                createdSchedule = new WeeklySchedule(name, new AcademyId((long) academyId));
                httpStatusCode = 201;
            } else {
                httpStatusCode = 400;
            }
        } catch (Exception e) {
            thrownException = e;
            httpStatusCode = 400;
        }
    }

    @Then("el horario queda creado exitosamente")
    public void el_horario_queda_creado_exitosamente() {
        assertNotNull(returnedId);
        assertNull(thrownException);
    }

    @Then("se devuelve un ID de horario")
    public void se_devuelve_un_id_de_horario() {
        assertNotNull(returnedId);
        assertEquals(1L, returnedId);
    }

    @Given("existe un horario semanal con ID {int} para la academia con ID {int}")
    public void existe_un_horario_semanal_con_id_para_la_academia(int scheduleId, int academyId) {
        WeeklySchedule mockSchedule = new WeeklySchedule("Semana 1", new AcademyId((long) academyId));

        when(queryService.handle(any(com.nistra.demy.platform.scheduling.domain.model.queries.GetWeeklyScheduleByIdQuery.class)))
                .thenReturn(Optional.of(mockSchedule));

        when(commandService.handle(any(AddScheduleToWeeklyCommand.class)))
                .thenAnswer(inv -> {
                    AddScheduleToWeeklyCommand cmd = inv.getArgument(0);
                    mockSchedule.addSchedule(
                            cmd.startTime(),
                            cmd.endTime(),
                            com.nistra.demy.platform.scheduling.domain.model.valueobjects.DayOfWeek.valueOf(cmd.dayOfWeek()),
                            cmd.courseId(),
                            cmd.classroomId(),
                            1L
                    );
                    return Optional.of(mockSchedule);
                });
    }

    @When("agrego una sesión de clase con inicio {string}, fin {string}, día {string}, curso ID {int}, salón ID {int}, profesor {string} {string}")
    public void agrego_una_sesion_de_clase_con_datos(String startTime, String endTime, String dayOfWeek, int courseId, int classroomId, String teacherFirstName, String teacherLastName) {
        try {
            AddScheduleToWeeklyCommand command = new AddScheduleToWeeklyCommand(
                    1L, startTime, endTime, dayOfWeek, (long) courseId, (long) classroomId, teacherFirstName, teacherLastName
            );

            Optional<WeeklySchedule> result = commandService.handle(command);
            if (result.isPresent()) {
                scheduleWithSession = result.get();
                httpStatusCode = 200;
            } else {
                httpStatusCode = 404;
            }
        } catch (Exception e) {
            thrownException = e;
            httpStatusCode = 500;
        }
    }

    @Then("la sesión queda agregada al horario exitosamente")
    public void la_sesion_queda_agregada_al_horario_exitosamente() {
        assertNotNull(scheduleWithSession);
    }

    @Then("el horario tiene al menos una sesión")
    public void el_horario_tiene_al_menos_una_sesion() {
        assertNotNull(scheduleWithSession);
        assertNotNull(scheduleWithSession.getSchedules());
        assertFalse(scheduleWithSession.getSchedules().isEmpty());
    }

    @Given("existe un horario semanal con ID {int} para la academia con ID {int} preparado para eliminar")
    public void existe_un_horario_semanal_con_id_para_la_academia_para_eliminar(int scheduleId, int academyId) {
        doNothing().when(commandService).handle(any(DeleteWeeklyScheduleCommand.class));
    }

    @When("elimino el horario semanal con ID {int}")
    public void elimino_el_horario_semanal_con_id(int scheduleId) {
        try {
            DeleteWeeklyScheduleCommand command = new DeleteWeeklyScheduleCommand((long) scheduleId);
            commandService.handle(command);
            httpStatusCode = 200;
            responseMessage = "Schedule deleted successfully";
        } catch (Exception e) {
            thrownException = e;
            httpStatusCode = 500;
        }
    }

    @Then("el horario es eliminado exitosamente")
    public void el_horario_es_eliminado_exitosamente() {
        assertEquals(200, httpStatusCode);
        assertNull(thrownException);
    }

    @Then("el mensaje de respuesta es {string}")
    public void el_mensaje_de_respuesta_es(String message) {
        assertEquals(message, responseMessage);
    }
}