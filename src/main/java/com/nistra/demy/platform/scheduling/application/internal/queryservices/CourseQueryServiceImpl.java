package com.nistra.demy.platform.scheduling.application.internal.queryservices;

import com.nistra.demy.platform.iam.interfaces.acl.IamContextFacade;
import com.nistra.demy.platform.institution.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.scheduling.application.internal.outboundservices.acl.ExternalInstitutionService;
import com.nistra.demy.platform.scheduling.domain.model.aggregates.Course;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetAllCoursesQuery;
import com.nistra.demy.platform.scheduling.domain.model.queries.GetCourseByIdQuery;
import com.nistra.demy.platform.scheduling.domain.services.CourseQueryService;
import com.nistra.demy.platform.scheduling.infrastructure.persistence.jpa.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Course Query Service Implementation
 * <p>This class implements the course query service interface and provides the business logic for handling course queries such as retrieving all courses and finding courses by ID.</p>
 */
@Service
public class CourseQueryServiceImpl implements CourseQueryService {

    private final CourseRepository courseRepository;
    private final ExternalIamService externalIamService;

    /**
     * Constructor that initializes the service with the required repository.
     * @param courseRepository The course repository.
     */
    public CourseQueryServiceImpl(CourseRepository courseRepository, ExternalIamService externalIamService) {
        this.courseRepository = courseRepository;
        this.externalIamService = externalIamService;
    }

    /**
     * This method is used to handle retrieving all courses.
     * @param query The get all courses query.
     * @return A list of all courses in the system.
     * @see GetAllCoursesQuery
     * @see Course
     */
    @Override
    public List<Course> handle(GetAllCoursesQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("No academy context found for the current user"));

        return courseRepository.findAllByAcademyId(academyId);
    }

    /**
     * This method is used to handle retrieving a course by its ID.
     * @param query The get course by ID query containing the course ID.
     * @return An optional with the course if found, otherwise an empty optional.
     * @see GetCourseByIdQuery
     * @see Course
     */
    @Override
    public Optional<Course> handle(GetCourseByIdQuery query) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalStateException("No academy context found for the current user"));

        return courseRepository.findById(query.courseId())
                .filter(course -> course.getAcademyId().equals(academyId));
    }
}