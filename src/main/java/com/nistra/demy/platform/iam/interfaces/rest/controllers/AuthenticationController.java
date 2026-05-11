package com.nistra.demy.platform.iam.interfaces.rest.controllers;

import com.nistra.demy.platform.iam.domain.services.UserCommandService;
import com.nistra.demy.platform.iam.interfaces.rest.resources.*;
import com.nistra.demy.platform.iam.interfaces.rest.transform.*;
import com.nistra.demy.platform.shared.interfaces.rest.resources.MessageResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Available Authentication Endpoints")
public class AuthenticationController {

    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Sign-in", description = "Sign-in with the provided credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")})
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource signInResource) {
        var signInCommand = SignInCommandFromResourceAssembler.toCommandFromResource(signInResource);
        var authenticatedUser = userCommandService.handle(signInCommand);
        if (authenticatedUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());
        return ResponseEntity.ok(authenticatedUserResource);
    }

    @PostMapping("/sign-up")
    @Operation(summary = "Sign-up", description = "Sign-up with the provided credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request.")})
    public ResponseEntity<UserResource> signUp(@RequestBody SignUpResource signUpResource) {
        var signUpCommand = SignUpCommandFromResourceAssembler.toCommandFromResource(signUpResource);
        var user = userCommandService.handle(signUpCommand);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Verify user account",
            description = "Verifies a user's account by validating the provided verification code."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User verified successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VerifiedUserResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid verification code or expired", content = @Content)
    })
    @PostMapping("/verify")
    public ResponseEntity<VerifiedUserResource> verify(@RequestBody VerifyUserResource verifyUserResource) {
        var verifyUserCommand = VerifyUserCommandFromResourceAssembler.toCommandFromResource(verifyUserResource);
        var verifiedUser = userCommandService.handle(verifyUserCommand);
        if (verifiedUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var verifiedUserResource = VerifiedUserResourceFromEntityAssembler.toResourceFromEntity(verifiedUser.get().getLeft(), verifiedUser.get().getRight());
        return ResponseEntity.ok(verifiedUserResource);
    }

    @Operation(
            summary = "Resend verification code",
            description = "Resends a new verification code to the user’s registered email address."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Verification code resent successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "400", description = "Unable to resend verification code", content = @Content)
    })
    @PostMapping("/resend-code")
    public ResponseEntity<MessageResource> resendCode(@RequestBody ResendVerificationCodeResource resendVerificationCodeResource) {
        var resendVerificationCodeCommand = ResendVerificationCodeCommandFromResourceAssembler.toCommandFromResource(resendVerificationCodeResource);
        userCommandService.handle(resendVerificationCodeCommand);
        return ResponseEntity.ok(new MessageResource("Verification code resent successfully!"));
    }

    @PostMapping("/request-reset-password")
    public ResponseEntity<MessageResource> requestResetPassword(@RequestBody RequestResetPasswordResource requestResetPasswordResource) {
        var requestResetPasswordCommand = RequestResetPasswordCommandFromResourceAssembler.toCommandFromResource(requestResetPasswordResource);
        userCommandService.handle(requestResetPasswordCommand);
        return ResponseEntity.ok(new MessageResource("If the email is registered, a password reset code has been sent."));
    }

    @Operation(
            summary = "Reset password",
            description = "Resets the user's password after verifying the reset code. Returns authentication token for immediate login."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResetPasswordResponseResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or verification code not verified", content = @Content)
    })
    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponseResource> resetPassword(@RequestBody ResetPasswordResource resetPasswordResource) {
        var resetPasswordCommand = ResetPasswordCommandFromResourceAssembler.toCommandFromResource(resetPasswordResource);
        var resetPasswordResult = userCommandService.handle(resetPasswordCommand);
        if (resetPasswordResult.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var resetPasswordResponseResource = ResetPasswordResponseResourceFromEntityAssembler.toResourceFromEntity(
                resetPasswordResult.get().getLeft(),
                resetPasswordResult.get().getRight()
        );
        return ResponseEntity.ok(resetPasswordResponseResource);
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<MessageResource> verifyResetCode(@RequestBody VerifyResetPasswordCodeResource verifyResetPasswordCodeResource) {
        var verifyResetPasswordCodeCommand = VerifyResetPasswordCodeCommandFromResourceAssembler.toCommandFromResource(verifyResetPasswordCodeResource);
        userCommandService.handle(verifyResetPasswordCodeCommand);
        return ResponseEntity.ok(new MessageResource("Password reset code verified successfully."));
    }
}
