package com.nistra.demy.platform.scheduling.interfaces.rest.controllers;

import com.nistra.demy.platform.scheduling.domain.model.commands.DeleteCourseCommand;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetAllCoursesQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetCourseByIdQuery;
import com.nistra.demy.platform.scheduling.domain.services.CourseCommandService;
import com.nistra.demy.platform.scheduling.domain.services.CourseQueryService;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.CourseResource;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.CreateCourseResource;
import com.nistra.demy.platform.scheduling.interfaces.rest.resources.UpdateCourseResource;
import com.nistra.demy.platform.scheduling.interfaces.rest.transform.CourseResourceFromEntityAssembler;
import com.nistra.demy.platform.scheduling.interfaces.rest.transform.CreateCourseCommandFromResourceAssembler;
import com.nistra.demy.platform.scheduling.interfaces.rest.transform.UpdateCourseCommandFromResourceAssembler;
import com.nistra.demy.platform.shared.interfaces.rest.resources.MessageResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing courses. It provides endpoints for creating,
 * retrieving, updating, and deleting courses.
 * This controller interacts with the {@link CourseCommandService} for handling
 * commands related to course creation, updating, and deletion, and the
 * {@link CourseQueryService} for handling queries related to retrieving
 * courses.
 */
@RestController
@RequestMapping(value = "/api/v1/courses", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Courses", description = "Course Management Endpoints")
public class CoursesController {

    private final CourseCommandService courseCommandService;
    private final CourseQueryService courseQueryService;

    public CoursesController(CourseCommandService courseCommandService, CourseQueryService courseQueryService) {
        this.courseCommandService = courseCommandService;
        this.courseQueryService = courseQueryService;
    }

    /**
     * Creates a new course based on the provided resource.
     *
     * @param resource The resource containing the details of the course to be created.
     * @return A {@link ResponseEntity} with the created course or an error response.
     */
    @PostMapping
    @Operation(summary = "Create a new course", description = "Create a new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseResource> createCourse(@RequestBody CreateCourseResource resource) {
        var createCourseCommand = CreateCourseCommandFromResourceAssembler.toCommandFromResource(resource);
        var courseId = courseCommandService.handle(createCourseCommand);
        if (courseId == null || courseId == 0L ) {
            return ResponseEntity.badRequest().build();
        }
        var getCourseByIdQuery = new GetCourseByIdQuery(courseId);
        var course = courseQueryService.handle(getCourseByIdQuery);
        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var courseEntity = course.get();
        var courseResource = CourseResourceFromEntityAssembler.toResourceFromEntity(courseEntity);
        return new ResponseEntity<>(courseResource, HttpStatus.CREATED);
    }

    /**
     * Retrieves all courses.
     *
     * @return A {@link ResponseEntity} containing a list of courses or an error response.
     */
    @GetMapping
    @Operation(summary = "Get all courses", description = "Get all courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses found"),
            @ApiResponse(responseCode = "404", description = "No courses found")
    })
    public ResponseEntity<List<CourseResource>> getAllCourses() {
        var getAllCoursesQuery = new GetAllCoursesQuery();
        var courses = courseQueryService.handle(getAllCoursesQuery);
//        if (courses.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
        var courseResources = courses.stream()
                .map(CourseResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(courseResources);
    }

    /**
     * Retrieves a course by its ID.
     *
     * @param courseId The ID of the course to retrieve.
     * @return A {@link ResponseEntity} containing the course resource or an error response.
     */
    @GetMapping("/{courseId}")
    @Operation(summary = "Get course by ID", description = "Get a course by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseResource> getCourseById(@PathVariable Long courseId) {
        var getCourseByIdQuery = new GetCourseByIdQuery(courseId);
        var course = courseQueryService.handle(getCourseByIdQuery);
        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var courseResource = CourseResourceFromEntityAssembler.toResourceFromEntity(course.get());
        return ResponseEntity.ok(courseResource);
    }

    /**
     * Updates a course by its ID.
     *
     * @param courseId The ID of the course to update.
     * @param resource The resource containing the updated course details.
     * @return A {@link ResponseEntity} containing the updated course or an error response.
     */
    @PutMapping("/{courseId}")
    @Operation(summary = "Update a course by ID", description = "Update a course by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<CourseResource> updateCourse(@PathVariable Long courseId, @RequestBody UpdateCourseResource resource) {
        var updateCourseCommand = UpdateCourseCommandFromResourceAssembler.toCommandFromResource(courseId, resource);
        var updatedCourse = courseCommandService.handle(updateCourseCommand);
        if (updatedCourse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedCourseEntity = updatedCourse.get();
        var courseResource = CourseResourceFromEntityAssembler.toResourceFromEntity(updatedCourseEntity);
        return ResponseEntity.ok(courseResource);
    }

    /**
     * Deletes a course by its ID.
     *
     * @param courseId The ID of the course to delete.
     * @return A {@link ResponseEntity} indicating the deletion status.
     */
    @DeleteMapping("/{courseId}")
    @Operation(summary = "Delete a course by ID", description = "Delete a course by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) {
        var deleteCourseCommand = new DeleteCourseCommand(courseId);
        courseCommandService.handle(deleteCourseCommand);
        return ResponseEntity.ok(new MessageResource("Course deleted successfully"));
    }
}
