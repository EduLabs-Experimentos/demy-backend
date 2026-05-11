package com.nistra.demy.platform.scheduling.application.internal.commandservices;

import com.nistra.demy.platform.institution.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.scheduling.domain.exceptions.ClassroomAlreadyExistsException;
import com.nistra.demy.platform.scheduling.domain.exceptions.ClassroomNotFoundException;
import com.nistra.demy.platform.scheduling.domain.model.aggregates.Classroom;
import com.nistra.demy.platform.scheduling.domain.model.commands.CreateClassroomCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.DeleteClassroomCommand;
import com.nistra.demy.platform.scheduling.domain.model.commands.UpdateClassroomCommand;
import com.nistra.demy.platform.scheduling.domain.services.ClassroomCommandService;
import com.nistra.demy.platform.scheduling.infrastructure.persistence.jpa.repositories.ClassroomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Classroom Command Service Implementation
 * <p>This class implements the classroom command service interface and provides the business logic for handling classroom commands such as create, update, and delete operations.</p>
 */
@Service
public class ClassroomCommandServiceImpl implements ClassroomCommandService {

    private final ClassroomRepository classroomRepository;
    private final ExternalIamService externalIamService;

    /**
     * Constructor that initializes the service with the required repository.
     * @param classroomRepository The classroom repository.
     */
    public ClassroomCommandServiceImpl(ClassroomRepository classroomRepository, ExternalIamService externalIamService) { // [MODIFICADO]
        this.classroomRepository = classroomRepository;
        this.externalIamService = externalIamService; // Inyectar ACL
    }

    /**
     * This method is used to handle the creation of a new classroom.
     * @param command The create classroom command containing the classroom data.
     * @return The ID of the created classroom.
     * @throws IllegalArgumentException if a classroom with the same code already exists or if there's an error saving the classroom.
     * @see CreateClassroomCommand
     * @see Classroom
     */
    @Override
    public Long handle(CreateClassroomCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalArgumentException("Academy context not found for the current user"));

        if (classroomRepository.existsByCodeAndAcademyId(command.code(),academyId)) {
            throw new ClassroomAlreadyExistsException(command.code());
        }

        var classroom = new Classroom(command,academyId);
        try {
            classroomRepository.save(classroom);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving classroom: %s".formatted(e.getMessage()));
        }
        return classroom.getId();
    }

    /**
     * This method is used to handle the update of an existing classroom.
     * @param command The update classroom command containing the updated classroom data.
     * @return An optional with the updated classroom if successful, otherwise an empty optional.
     * @throws IllegalArgumentException if a classroom with the same code already exists, if the classroom to update is not found, or if there's an error updating the classroom.
     * @see UpdateClassroomCommand
     * @see Classroom
     */
    @Override
    public Optional<Classroom> handle(UpdateClassroomCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalArgumentException("Academy context not found for the current user"));

        if (classroomRepository.existsByCodeAndIdNotAndAcademyId(command.code(), command.classroomId(), academyId)) {
            throw new ClassroomAlreadyExistsException(command.code());
        }

        var result = classroomRepository.findById(command.classroomId());
        if (result.isEmpty()) {
            throw new ClassroomNotFoundException(command.classroomId());
        }

        var classroomToUpdate = result.get();

        if (!classroomToUpdate.getAcademyId().equals(academyId)) {
            throw new ClassroomNotFoundException(command.classroomId());
        }

        try {
            var updatedClassroom = classroomRepository.save(classroomToUpdate.updateClassroom(command));
            return Optional.of(updatedClassroom);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating classroom: " + e.getMessage());
        }
    }

    /**
     * This method is used to handle the deletion of an existing classroom.
     * @param command The delete classroom command containing the classroom ID to delete.
     * @throws IllegalArgumentException if the classroom to delete is not found or if there's an error deleting the classroom.
     * @see DeleteClassroomCommand
     */
    @Override
    public void handle(DeleteClassroomCommand command) {
        var academyId = externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalArgumentException("Academy context not found for the current user"));

        var classroom = classroomRepository.findById(command.classroomId())
                .orElseThrow(() -> new ClassroomNotFoundException(command.classroomId()));

        if (!classroom.getAcademyId().equals(academyId)) {
            throw new IllegalArgumentException("Classroom with id " + command.classroomId() + " does not belong to the current academy");
        }

        try {
            classroomRepository.deleteById(command.classroomId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deleting classroom: " + e.getMessage(), e);
        }
    }
}
