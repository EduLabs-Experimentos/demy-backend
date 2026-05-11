// AttendanceSteps.java
package com.nistra.demy.platform.acceptance.test.steps;

import com.nistra.demy.platform.attendance.domain.model.aggregates.ClassAttendance;
import com.nistra.demy.platform.attendance.domain.model.commands.CreateClassAttendanceCommand;
import com.nistra.demy.platform.attendance.domain.model.entities.AttendanceRecord;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceInput;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.AttendanceStatus;
import com.nistra.demy.platform.attendance.domain.model.valueobjects.ClassSessionId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AttendanceSteps {

    @Autowired
    private AttendanceTestService attendanceTestService;

    private ClassAttendance currentAttendance;
    private Exception       capturedException;
    private AcademyId       pendingAcademyId;
    private ClassSessionId  pendingSessionId;

    @Before
    public void resetState() {
        capturedException = null;
        currentAttendance = null;
        attendanceTestService.deleteAll();
    }

    @Given("an academy with ID {long} and a class session with ID {long} exists for today's date")
    public void anAcademyWithIdAndAClassSessionWithIdExistsForTodaysDate(long academyId, long sessionId) {
        currentAttendance = null;
        this.pendingAcademyId = new AcademyId(academyId);
        this.pendingSessionId = new ClassSessionId(sessionId);
    }

    @And("the following students are enrolled in the session:")
    public void theFollowingStudentsAreEnrolledInTheSession(DataTable dataTable) {
        List<AttendanceInput> inputs = dataTable.asMaps().stream()
                .map(row -> new AttendanceInput(
                        new DniNumber(row.get("dni")),
                        AttendanceStatus.valueOf(row.get("initialStatus"))
                ))
                .toList();

        CreateClassAttendanceCommand command = new CreateClassAttendanceCommand(
                pendingSessionId, LocalDate.now(), inputs
        );
        currentAttendance = attendanceTestService.createAttendance(pendingAcademyId, command);
    }

    @Given("the student with DNI {string} has status {string}")
    public void theStudentWithDniHasStatus(String dniStr, String status) {
        DniNumber dni = new DniNumber(dniStr);
        AttendanceRecord record = currentAttendance.getRecordByDniOrThrow(dni);
        assertThat(record.getStatus()).isEqualTo(AttendanceStatus.valueOf(status));
    }

    @Given("the student with DNI {string} is not enrolled in the session")
    public void theStudentWithDniIsNotEnrolledInTheSession(String dniStr) {
        DniNumber dni = new DniNumber(dniStr);
        boolean enrolled = currentAttendance.getAttendance().stream()
                .anyMatch(r -> r.getDni().equals(dni));
        assertThat(enrolled)
                .as("Student with DNI %s should NOT be enrolled", dniStr)
                .isFalse();
    }

    @When("the teacher marks the student with DNI {string} as {string}")
    public void theTeacherMarksTheStudentWithDniAs(String dniStr, String status) {
        try {
            currentAttendance = attendanceTestService.updateStatus(
                    currentAttendance.getId(),
                    pendingAcademyId,
                    new DniNumber(dniStr),
                    AttendanceStatus.valueOf(status)
            );
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @Then("the attendance record for DNI {string} should have status {string}")
    public void theAttendanceRecordForDniShouldHaveStatus(String dniStr, String expectedStatus) {
        ClassAttendance reloaded = attendanceTestService.reloadWithRecords(
                currentAttendance.getId(), pendingAcademyId
        );
        DniNumber dni = new DniNumber(dniStr);
        AttendanceStatus actual = reloaded.getRecordByDniOrThrow(dni).getStatus();
        assertThat(actual).isEqualTo(AttendanceStatus.valueOf(expectedStatus));
    }

    @Then("an error should be raised indicating that the DNI was not found")
    public void anErrorShouldBeRaisedIndicatingThatTheDniWasNotFound() {
        assertThat(capturedException)
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(capturedException.getMessage())
                .containsIgnoringCase("does not exist");
    }
}