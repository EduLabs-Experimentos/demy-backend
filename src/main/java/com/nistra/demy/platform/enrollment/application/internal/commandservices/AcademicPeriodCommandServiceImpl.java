package com.nistra.demy.platform.enrollment.application.internal.commandservices;

import com.nistra.demy.platform.enrollment.application.internal.outboundservices.acl.ExternalIamService;
import com.nistra.demy.platform.enrollment.domain.exceptions.AcademicPeriodAlreadyExistsException;
import com.nistra.demy.platform.enrollment.domain.exceptions.AcademicPeriodNotFoundException;
import com.nistra.demy.platform.enrollment.domain.model.aggregates.AcademicPeriod;
import com.nistra.demy.platform.enrollment.domain.model.commands.CreateAcademicPeriodCommand;
import com.nistra.demy.platform.enrollment.domain.model.commands.DeleteAcademicPeriodCommand;
import com.nistra.demy.platform.enrollment.domain.model.commands.UpdateAcademicPeriodCommand;
import com.nistra.demy.platform.enrollment.domain.services.AcademicPeriodCommandService;
import com.nistra.demy.platform.enrollment.infrastructure.persistence.jpa.repositories.AcademicPeriodRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.AcademyId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AcademicPeriodCommandServiceImpl implements AcademicPeriodCommandService {
    private final AcademicPeriodRepository academicPeriodRepository;
    private final ExternalIamService externalIamService;

    public AcademicPeriodCommandServiceImpl(
            ExternalIamService externalIamService,
            AcademicPeriodRepository academicPeriodRepository){
        this.academicPeriodRepository = academicPeriodRepository;
        this.externalIamService = externalIamService;
    }

    private AcademyId getContextAcademyId() {
        return externalIamService.fetchCurrentAcademyId()
                .orElseThrow(() -> new IllegalArgumentException("Academy not found"));
    }

    @Override
    public Long handle(CreateAcademicPeriodCommand command) {
        var academyId = getContextAcademyId();

        if (academicPeriodRepository.existsByPeriodName(command.periodName())) {
            throw new AcademicPeriodAlreadyExistsException(command.periodName());
        }

        var academicPeriod = AcademicPeriod.createActiveAcademicPeriod(
                command.periodName(),
                command.startDate(),
                command.endDate(),
                academyId
        );

        try {
            academicPeriodRepository.save(academicPeriod);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create academic period: " + e.getMessage(), e);
        }
        return academicPeriod.getId();
    }

    @Override
    public void handle(DeleteAcademicPeriodCommand command) {
        var academyId = getContextAcademyId();

        var academicPeriod = academicPeriodRepository.findById(command.academicPeriodId())
                .orElseThrow(() -> new AcademicPeriodNotFoundException(command.academicPeriodId()));

        if (!academicPeriod.getAcademyId().equals(academyId)) {
            throw new AcademicPeriodNotFoundException(command.academicPeriodId());
        }

        try {
            academicPeriodRepository.deleteById(command.academicPeriodId());
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting academic period" + e.getMessage(), e);
        }
    }

    @Override
    public Optional<AcademicPeriod> handle(UpdateAcademicPeriodCommand command) {
        var academyId = getContextAcademyId();

        if (academicPeriodRepository.existsByPeriodNameAndIdIsNot(command.periodName(), command.academicPeriodId())) {
            throw new AcademicPeriodAlreadyExistsException(command.periodName());
        }

        var academicPeriod = academicPeriodRepository.findById(command.academicPeriodId())
                .orElseThrow(() -> new AcademicPeriodNotFoundException(command.academicPeriodId()));;

        if (!academicPeriod.getAcademyId().equals(academyId)) {
            throw new AcademicPeriodNotFoundException(command.academicPeriodId());
        }

        try {
            var updatedAcademicPeriod = academicPeriodRepository.save(
                    academicPeriod.updateInformation(
                        command.periodName(),
                        command.startDate(),
                        command.endDate(),
                        command.isActive()
                    )
            );
            return Optional.of(updatedAcademicPeriod);
        } catch (Exception e) {
            throw new RuntimeException("Error while updating academic period: " + e.getMessage(), e);
        }
    }
}
