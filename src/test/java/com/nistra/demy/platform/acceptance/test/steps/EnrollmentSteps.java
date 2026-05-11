package com.nistra.demy.platform.acceptance.test.steps;

import com.nistra.demy.platform.enrollment.domain.model.aggregates.Enrollment;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.*;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.Money;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.*;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EnrollmentSteps {
    private Long studentId;
    private Long periodId;
    private Long scheduleId;
    private Long academyId;
    private Enrollment enrollment;
    private Exception exception;
    private String message;

    @Given("existe un Student con id {long}")
    public void existe_un_student_con_id(Long id) {
        this.studentId = id;
    }

    @Given("existe un AcademicPeriod con id {long}")
    public void existe_un_academic_period_con_id(Long id) {
        this.periodId = id;
    }


    @Given("existe un WeeklySchedule con id {long}")
    public void existe_un_weekly_schedule_con_id(Long id) { this.scheduleId = id; }

    @Given("existe un Academy con id {long}")
    public void existe_un_academy_con_id_academy_con_id(Long id) { this.academyId = id; }

    @When("intento registrar la matrícula con amount {bigDecimal} y currency {word}")
    public void intento_registrar_la_matricula(BigDecimal amount, String currencyCode) {
        try {
            enrollment = Enrollment.createEnrollmentActive(
                    new StudentId(this.studentId),
                    new PeriodId(this.periodId),
                    new ScheduleId(this.scheduleId),
                    new AcademyId(this.academyId),
                    new Money(amount, Currency.getInstance(currencyCode)),
                    PaymentStatus.PAID
            );
            message = "Test Passed";
        } catch (Exception e) {
            this.exception = e;
            // si el dominio lanza un mensaje que empieza por "Error:", lo dejamos entero
            message = e.getMessage().startsWith("Error:") ? e.getMessage() : "Error";
        }
    }

    @Then("debe crearse una Enrollment con")
    public void debe_crearse_una_enrollment_con(DataTable table) {
        if (exception != null) {
            return;
        }

        Map<String, String> rawMap = table.asMap();

        Map<String, String> map = new HashMap<>();
        rawMap.forEach((key, value) -> {
            String cleanKey = key.trim();
            String cleanValue = value.replaceAll("^\"|\"$", "").trim();
            map.put(cleanKey, cleanValue);
        });

        assertEquals(Long.valueOf(map.get("studentId")), enrollment.getStudentId().studentId());
        assertEquals(Long.valueOf(map.get("academicPeriodId")), enrollment.getPeriodId().periodId());
        assertEquals(Long.valueOf(map.get("weeklyScheduleId")), enrollment.getScheduleId().scheduleId());
        assertEquals(Long.valueOf(map.get("academyId")), enrollment.getAcademyId().academyId());
        assertEquals(new BigDecimal(map.get("amount")), enrollment.getMoney().amount());
        assertEquals(
                Currency.getInstance(map.get("currency")).getCurrencyCode(),
                enrollment.getMoney().currency().getCurrencyCode()
        );
        assertEquals(map.get("status"), enrollment.getEnrollmentStatus().name());
    }


    @Then("el mensaje final es {string}")
    public void el_mensaje_final_es(String expectedMessage) {
        assertEquals(expectedMessage, message);
    }

    @ParameterType("[-+]?[0-9]*\\.?[0-9]+")
    public BigDecimal bigDecimal(String value) {
        return new BigDecimal(value);
    }

}

