package com.nistra.demy.platform.iam.domain.model.aggregates;


import com.nistra.demy.platform.iam.domain.model.entities.Role;
import com.nistra.demy.platform.iam.domain.model.events.UserNewPasswordVerificationCodeAssignedEvent;
import com.nistra.demy.platform.iam.domain.model.events.UserSignedUpAndActivatedEvent;
import com.nistra.demy.platform.iam.domain.model.events.UserVerificationCodeAssignedEvent;
import com.nistra.demy.platform.iam.domain.model.valueobjects.AccountStatus;
import com.nistra.demy.platform.iam.domain.model.valueobjects.TenantId;
import com.nistra.demy.platform.iam.domain.model.valueobjects.VerificationCode;
import com.nistra.demy.platform.iam.domain.model.valueobjects.VerificationStatus;
import com.nistra.demy.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class User extends AuditableAbstractAggregateRoot<User> {
    @Embedded
    private EmailAddress emailAddress;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Embedded
    private VerificationCode verificationCode;

    @Embedded
    private TenantId tenantId;

    public User() {}

    public User(EmailAddress emailAddress, String password, VerificationCode verificationCode) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.verificationCode = verificationCode;
        this.verificationStatus = VerificationStatus.NOT_VERIFIED;
        this.accountStatus = AccountStatus.PENDING;
        this.roles = new HashSet<>();
        this.tenantId = new TenantId();
    }

    public User(EmailAddress emailAddress, String password, VerificationCode verificationCode, List<Role> roles) {
        this(emailAddress, password, verificationCode);
        addRoles(roles);
    }

    public User(EmailAddress emailAddress, String password, List<Role> roles, TenantId tenantId) {
        this(emailAddress, password, new VerificationCode(null, null), roles);
        this.verificationStatus = VerificationStatus.VERIFIED;
        this.accountStatus = AccountStatus.ACTIVE;
        this.tenantId = tenantId;
    }

    public User addRole(Role role) {
        this.roles.add(role);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public User addRoles(List<Role> roles) {
        var validateRoleSet = Role.validateRoleSet(roles);
        this.roles.addAll(validateRoleSet);
        return this;
    }

    public boolean isVerified() {
        return verificationStatus == VerificationStatus.VERIFIED;
    }

    public void activate() {
        if (verificationStatus != VerificationStatus.VERIFIED)
            throw new IllegalStateException("User must be verified before activation");
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public void assignVerificationCode(String email, String code, Integer expirationMinutes) {
        this.verificationCode = new VerificationCode(code, LocalDateTime.now().plusMinutes(expirationMinutes));
        this.addDomainEvent(new UserVerificationCodeAssignedEvent(
                this,
                email,
                code,
                expirationMinutes));
    }

    public void verifyUser(String code) {
        if (this.isVerified()) throw new IllegalStateException("User is already verified");
        if (!this.verificationCode.matches(code)) throw new IllegalArgumentException("Invalid verification code");
        this.verificationCode = new VerificationCode(null, null);
        this.verificationStatus = VerificationStatus.VERIFIED;
        this.activate();
    }

    public void notifySignedUpAndActivated(String email, String password) {
        this.addDomainEvent(new UserSignedUpAndActivatedEvent(
                this,
                email,
                password));
    }

    public void assignNewPasswordVerificationCode(String email, String code, Integer expirationMinutes) {
        this.verificationCode = new VerificationCode(code, LocalDateTime.now().plusMinutes(expirationMinutes));
        this.addDomainEvent(new UserNewPasswordVerificationCodeAssignedEvent(
                this,
                email,
                code,
                expirationMinutes));
    }

    public void verifyPasswordResetCode(String code) {
        if (!this.verificationCode.matches(code)) throw new IllegalArgumentException("Invalid verification code");
        this.verificationCode = new VerificationCode(null, null);
    }

    public void resetPassword(String newPassword) {
        this.password = newPassword;
    }

    public void associateTenant(TenantId tenantId) {
        if (this.tenantId != null && this.tenantId.isAssigned())
            throw new IllegalStateException("User is already associated with a tenant");
        this.tenantId = tenantId;
    }

    public void disassociateTenant(TenantId tenantId) {
        if (this.tenantId == null || !this.tenantId.equals(tenantId)) {
            throw new IllegalStateException("User is not associated with the provided tenant");
        }
        this.tenantId = new TenantId();
    }
}
