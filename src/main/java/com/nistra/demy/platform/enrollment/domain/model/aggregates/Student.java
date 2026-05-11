package com.nistra.demy.platform.enrollment.domain.model.aggregates;

import com.nistra.demy.platform.enrollment.domain.model.commands.CreateStudentCommand;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.Sex;
import com.nistra.demy.platform.enrollment.domain.model.valueobjects.UserId;
import com.nistra.demy.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.nistra.demy.platform.shared.domain.model.valueobjects.*;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;


@Entity
public class Student extends AuditableAbstractAggregateRoot<Student> {
    @Embedded
    @Getter
    private PersonName personName;

    @Embedded
    @Getter
    private DniNumber dni;


    @Enumerated(EnumType.STRING)
    @Getter
    private Sex sex;

    @Getter
    private LocalDate birthDate;

    @Embedded
    @Getter
    private StreetAddress address;

    @Embedded
    @Getter
    private PhoneNumber phoneNumber;

    @Embedded
    @Getter
    private AcademyId academyId;

    @Embedded
    @Getter
    private UserId userId;

    /**
     * Default constructor for JPA
     */
    protected Student() {}

    /**
     * Constructor for creating a new instance of Student.
     *
     * @param firstName     The student's first name.
     * @param lastName      The student's last name.
     * @param dni           The student's national ID number.
     * @param sex           The student's biological sex.
     * @param birthDate     The student's birthdate.
     * @param street        The street of the student's address.
     * @param district      The district of the student's address.
     * @param province      The province of the student's address.
     * @param department    The department of the student's address.
     * @param countryCode   The country code of the student's phone number.
     * @param phoneNumber   The student's phone number.
     */
    public Student(
            String firstName,
            String lastName,
            String dni,
            String sex,
            LocalDate birthDate,
            String street,
            String district,
            String province,
            String department,
            String countryCode,
            String phoneNumber,
            Long academyId,
            Long userId) {
        this();
        this.personName = new PersonName(firstName, lastName);
        this.dni = new DniNumber(dni);
        this.sex = Sex.valueOf(sex.toUpperCase());;
        this.birthDate = birthDate;
        this.address = new StreetAddress(street, district, province, department);
        this.phoneNumber = new PhoneNumber(countryCode, phoneNumber);
        this.academyId = new AcademyId(academyId);
        this.userId = new UserId(userId);
    }

    public Student(
            PersonName personName,
            DniNumber dni,
            Sex sex,
            LocalDate birthDate,
            StreetAddress streetAddress,
            PhoneNumber phoneNumber,
            AcademyId academyId,
            UserId userId) {
        this();
        this.personName = personName;
        this.dni = dni;
        this.sex = sex;
        this.birthDate = birthDate;
        this.address = streetAddress;
        this.phoneNumber = phoneNumber;
        this.academyId = academyId;
        this.userId = userId;
    }

    /**
     * Constructor for creating a new Student from command data
     */
    public Student(CreateStudentCommand command, AcademyId academyId, UserId userId) {
        this();
        this.personName = command.personName();
        this.dni = command.dni();
        this.sex = command.sex();
        this.birthDate = command.birthDate();
        this.address = command.streetAddress();
        this.phoneNumber = command.phoneNumber();
        this.academyId = academyId;
        this.userId = userId;
    }

    /**
     * Update student information
     */
    public Student updateInformation(
            PersonName personName,
            DniNumber dniNumber,
            Sex sex,
            LocalDate birthDate,
            StreetAddress streetAddress,
            PhoneNumber phoneNumber) {
        this.personName = personName;
        this.dni = dniNumber;
        this.sex = sex;
        this.birthDate = birthDate;
        this.address = streetAddress;
        this.phoneNumber = phoneNumber;
        return this;
    }
}
