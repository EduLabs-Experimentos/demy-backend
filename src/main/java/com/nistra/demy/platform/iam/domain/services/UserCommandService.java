package com.nistra.demy.platform.iam.domain.services;

import com.nistra.demy.platform.iam.domain.model.aggregates.User;
import com.nistra.demy.platform.iam.domain.model.commands.*;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;

public interface UserCommandService {

    Optional<ImmutablePair<User, String>> handle(SignInCommand command);

    Optional<User> handle(SignUpCommand command);

    Optional<ImmutablePair<User, String>> handle(VerifyUserCommand command);

    Optional<User> handle(SignUpVerifiedUserCommand command);

    boolean handle(ResendVerificationCodeCommand command);

    void handle(RequestResetPasswordCommand command);

    boolean handle(VerifyResetPasswordCodeCommand command);

    Optional<ImmutablePair<User, String>> handle(ResetPasswordCommand command);

    void handle(AssignUserTenantId command);
}
