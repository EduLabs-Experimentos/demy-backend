package com.nistra.demy.platform.enrollment.interfaces.rest.controllers;

import com.nistra.demy.platform.enrollment.domain.model.commands.DeleteStudentCommand;
import com.nistra.demy.platform.enrollment.domain.model.queries.*;
import com.nistra.demy.platform.enrollment.domain.services.StudentCommandService;
import com.nistra.demy.platform.enrollment.domain.services.StudentQueryService;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.CreateStudentResource;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.StudentResource;
import com.nistra.demy.platform.enrollment.interfaces.rest.resources.UpdateStudentResource;
import com.nistra.demy.platform.enrollment.interfaces.rest.transform.CreateStudentCommandFromResourceAssembler;
import com.nistra.demy.platform.enrollment.interfaces.rest.transform.StudentResourceFromEntityAssembler;
import com.nistra.demy.platform.enrollment.interfaces.rest.transform.UpdateStudentCommandFromResourceAssembler;
import com.nistra.demy.platform.shared.domain.model.valueobjects.DniNumber;
import com.nistra.demy.platform.shared.interfaces.rest.resources.MessageResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * REST Controller for managing students.
 * Provides endpoints to create, retrieve, update, and delete student records.
 */

@RestController
@RequestMapping(value = "/api/v1/students", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Students", description = "Available Students Endpoints")
public class StudentsController {

    private final StudentCommandService studentCommandService;
    private final StudentQueryService studentQueryService;

    /**
     * Constructs the StudentsController with the required services.
     *
     * @param studentCommandService service to handle student commands (create, update, delete)
     * @param studentQueryService service to handle student queries (fetch students)
     */
    public StudentsController(StudentCommandService studentCommandService, StudentQueryService studentQueryService) {
        this.studentCommandService = studentCommandService;
        this.studentQueryService = studentQueryService;
    }

    /**
     * Creates a new student.
     *
     * @param resource the request body containing student data
     * @return ResponseEntity with the created student resource or an error status
     */
    @PostMapping
    @Operation(summary = "Create a new student", description = "Create a new student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Student with given DNI already exists")
    })
    public ResponseEntity<StudentResource> createStudent(@RequestBody CreateStudentResource resource) {
        var createCommand = CreateStudentCommandFromResourceAssembler.toCommandFromResource(resource);

        var studentId = studentCommandService.handle(createCommand);
        if (studentId == null || studentId == 0L) return ResponseEntity.badRequest().build();

        var student = studentQueryService.handle(new GetStudentByIdQuery(studentId));
        if (student.isEmpty()) return ResponseEntity.notFound().build();
        var email = studentQueryService.handle(new GetStudentEmailAddressByUserIdQuery(student.get().getUserId()));
        if (email.isEmpty()) return ResponseEntity.badRequest().build();

        var studentResource = StudentResourceFromEntityAssembler.toResourceFromEntity(student.get(), email.get());
        return new ResponseEntity<>(studentResource, HttpStatus.CREATED);
    }

    /**
     * Retrieves all registered students.
     *
     * @return a list of student resources or not found status if empty
     */
    @GetMapping
    @Operation(summary = "Get all students", description = "Get all registered students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students found"),
            @ApiResponse(responseCode = "404", description = "No students found")
    })
    public ResponseEntity<List<StudentResource>> getAllStudents() {
        var students = studentQueryService.handle(new GetAllStudentsQuery());
        // if (students.isEmpty()) return ResponseEntity.notFound().build();

        var resources = students
                .stream()
                .map(student -> {
                            var email = studentQueryService.handle(new GetStudentEmailAddressByUserIdQuery(student.getUserId()));
                            return email.map(emailAddress ->
                                    StudentResourceFromEntityAssembler.toResourceFromEntity(student, emailAddress)).orElse(null);
                }).filter(Objects::nonNull).toList();

        return ResponseEntity.ok(resources);
    }

    /**
     * Retrieves a specific student by their ID.
     *
     * @param studentId the ID of the student
     * @return the student resource or not found status
     */
    @GetMapping("/{studentId}")
    @Operation(summary = "Get student by ID", description = "Retrieve a student by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<StudentResource> getStudentById(@PathVariable Long studentId) {
        var student = studentQueryService.handle(new GetStudentByIdQuery(studentId));
        if (student.isEmpty()) return ResponseEntity.notFound().build();
        var email = studentQueryService.handle(new GetStudentEmailAddressByUserIdQuery(student.get().getUserId()));
        if (email.isEmpty()) return ResponseEntity.badRequest().build();

        var resource = StudentResourceFromEntityAssembler.toResourceFromEntity(student.get(), email.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Retrieves a student by their DNI.
     *
     * @param dni the DNI of the student
     * @return the student resource or not found status
     */
    @GetMapping("/dni/{dni}")
    @Operation(summary = "Get student by DNI", description = "Retrieve a student by their DNI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<StudentResource> getStudentByDni(@PathVariable String dni) {
        var student = studentQueryService.handle(new GetStudentByDniQuery(new DniNumber(dni)));
        if (student.isEmpty()) return ResponseEntity.notFound().build();
        var email = studentQueryService.handle(new GetStudentEmailAddressByUserIdQuery(student.get().getUserId()));
        if (email.isEmpty()) return ResponseEntity.badRequest().build();

        var resource = StudentResourceFromEntityAssembler.toResourceFromEntity(student.get(), email.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Updates an existing student's information.
     *
     * @param studentId the ID of the student to update
     * @param resource the request body with updated student data
     * @return the updated student resource or not found status
     */
    @PutMapping("/{studentId}")
    @Operation(summary = "Update student", description = "Update a student's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<StudentResource> updateStudent(@PathVariable Long studentId, @RequestBody UpdateStudentResource resource) {
        var updateCommand = UpdateStudentCommandFromResourceAssembler.toCommandFromResource(studentId, resource);
        var updatedStudent = studentCommandService.handle(updateCommand);
        if (updatedStudent.isEmpty()) return ResponseEntity.notFound().build();
        var email = studentQueryService.handle(new GetStudentEmailAddressByUserIdQuery(updatedStudent.get().getUserId()));
        if (email.isEmpty()) return ResponseEntity.badRequest().build();


        var studentResource = StudentResourceFromEntityAssembler.toResourceFromEntity(updatedStudent.get(), email.get());
        return ResponseEntity.ok(studentResource);
    }

    /**
     * Deletes a student by their ID.
     *
     * @param studentId the ID of the student to delete
     * @return a message indicating the student was successfully deleted
     */
    @DeleteMapping("/{studentId}")
    @Operation(summary = "Delete student", description = "Delete a student by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<?> deleteStudent(@PathVariable Long studentId) {
        var deleteCommand = new DeleteStudentCommand(studentId);
        studentCommandService.handle(deleteCommand);
        return ResponseEntity.ok(new MessageResource("Student successfully deleted"));
    }

    /**
     * Retrieves the currently authenticated student's information.
     *
     * @return the current student resource or not found status
     */
    @GetMapping("/me")
    @Operation(summary = "Get current student", description = "Retrieve the currently authenticated student's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Current student found"),
            @ApiResponse(responseCode = "404", description = "Current student not found")
    })
    public ResponseEntity<StudentResource> getCurrentAdministrator() {
        var getCurrentStudentQuery = new GetCurrentStudentQuery();
        var student = studentQueryService.handle(getCurrentStudentQuery);
        if (student.isEmpty()) return ResponseEntity.notFound().build();
        var studentEntity = student.get();
        var getStudentEmailAddressByUserIdQuery = new GetStudentEmailAddressByUserIdQuery(studentEntity.getUserId());
        var emailAddress = studentQueryService.handle(getStudentEmailAddressByUserIdQuery);
        if (emailAddress.isEmpty()) return ResponseEntity.notFound().build();
        var emailAddressValueObject = emailAddress.get();
        var currentStudentResource = StudentResourceFromEntityAssembler.toResourceFromEntity(studentEntity, emailAddressValueObject);
        return new ResponseEntity<>(currentStudentResource, HttpStatus.OK);
    }
}
