package com.nistra.demy.platform.iam.application.internal.commandservices;

import com.nistra.demy.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.nistra.demy.platform.iam.application.internal.outboundservices.identity.IdentityService;
import com.nistra.demy.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.nistra.demy.platform.iam.application.internal.outboundservices.verification.VerificationService;
import com.nistra.demy.platform.iam.domain.exceptions.UserNotFoundException;
import com.nistra.demy.platform.iam.domain.model.aggregates.User;
import com.nistra.demy.platform.iam.domain.model.commands.*;
import com.nistra.demy.platform.iam.domain.model.valueobjects.Roles;
import com.nistra.demy.platform.iam.domain.model.valueobjects.TenantId;
import com.nistra.demy.platform.iam.domain.model.valueobjects.VerificationCode;
import com.nistra.demy.platform.iam.domain.services.UserCommandService;
import com.nistra.demy.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.nistra.demy.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final VerificationService verificationService;
    private final IdentityService identityService;

    private final RoleRepository roleRepository;

    public UserCommandServiceImpl(
            UserRepository userRepository,
            HashingService hashingService,
            TokenService tokenService,
            VerificationService verificationService,
            IdentityService identityService,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.verificationService = verificationService;
        this.identityService = identityService;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByEmailAddress(command.emailAddress());
        if (user.isEmpty())
            throw new RuntimeException("User not found");
        if (!hashingService.matches(command.password(), user.get().getPassword()))
            throw new RuntimeException("Invalid password");
        var token = tokenService.generateToken(user.get().getEmailAddress().email());
        return Optional.of(ImmutablePair.of(user.get(), token));
    }

    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByEmailAddress(command.emailAddress()))
            throw new RuntimeException("Username already exists");
        var roles = (command.roles() == null || command.roles().isEmpty())
                ? List.of(roleRepository.findByName(Roles.ROLE_USER).orElseThrow(() -> new RuntimeException("Default role not found")))
                : command.roles().stream()
                .map(role -> roleRepository.findByName(role.getName()).orElseThrow(() -> new RuntimeException("Role name not found")))
                .toList();
        var code = verificationService.generateCode();
        var expirationMinutes = verificationService.generateExpirationMinutes();
        var verificationCode = new VerificationCode(code, LocalDateTime.now().plusMinutes(expirationMinutes));
        var user = new User(command.emailAddress(), hashingService.encode(command.password()), verificationCode, roles);
        user.assignVerificationCode(command.emailAddress().email(), code, expirationMinutes);
        userRepository.save(user);
        return userRepository.findByEmailAddress(command.emailAddress());
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(VerifyUserCommand command) {
        var user = userRepository.findByEmailAddress(new EmailAddress(command.email()))
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.verifyUser(command.code());
        userRepository.save(user);
        var token = tokenService.generateToken(user.getEmailAddress().email());
        return Optional.of(ImmutablePair.of(user, token));
    }

    @Override
    public Optional<User> handle(SignUpVerifiedUserCommand command) {
        if (userRepository.existsByEmailAddress(command.emailAddress()))
            throw new RuntimeException("Verified username already exists");
        var roles = (command.roles() == null || command.roles().isEmpty())
                ? List.of(roleRepository.findByName(Roles.ROLE_USER).orElseThrow(() -> new RuntimeException("Default role not found")))
                : command.roles().stream()
                .map(role -> roleRepository.findByName(role.getName()).orElseThrow(() -> new RuntimeException("Role name not found")))
                .toList();
        var tenantId = new TenantId(identityService.getTenantId()
                .orElseThrow(() -> new RuntimeException("Tenant ID not found in identity service")));
        var user = new User(
                command.emailAddress(),
                hashingService.encode(command.password()),
                roles,
                tenantId
        );
        user.notifySignedUpAndActivated(command.emailAddress().email(), command.password());
        userRepository.save(user);
        return userRepository.findByEmailAddress(command.emailAddress());
    }

    @Override
    public boolean handle(ResendVerificationCodeCommand command) {
        var user = userRepository.findByEmailAddress(new EmailAddress(command.email()))
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isVerified()) throw new RuntimeException("User is already verified");
        user.assignVerificationCode(command.email(), verificationService.generateCode(), verificationService.generateExpirationMinutes());
        userRepository.save(user);
        return true;
    }

    @Override
    public void handle(RequestResetPasswordCommand command) {
        var user = userRepository.findByEmailAddress(command.emailAddress())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var verificationCode = verificationService.generateCode();
        user.assignNewPasswordVerificationCode(
                command.emailAddress().email(),
                verificationCode,
                verificationService.generateExpirationMinutes()
        );
        userRepository.save(user);
    }

    @Override
    public boolean handle(VerifyResetPasswordCodeCommand command) {
        var user = userRepository.findByEmailAddress(command.emailAddress())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.verifyPasswordResetCode(command.code());
        userRepository.save(user);
        return true;
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(ResetPasswordCommand command) {
        var user = userRepository.findByEmailAddress(command.emailAddress())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.resetPassword(hashingService.encode(command.password()));
        userRepository.save(user);

        var token = tokenService.generateToken(user.getEmailAddress().email());
        return Optional.of(ImmutablePair.of(user, token));
    }

    @Override
    public void handle(AssignUserTenantId command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));
        try {
            user.associateTenant(new TenantId(command.tenantId()));
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to assign tenant to user: %s".formatted(e.getMessage()));
        }
    }
}
