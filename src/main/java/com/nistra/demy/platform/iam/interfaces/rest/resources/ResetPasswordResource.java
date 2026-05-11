package com.nistra.demy.platform.iam.interfaces.rest.resources;

public record ResetPasswordResource(
        String emailAddress,
        String password,
        String confirmPassword
) {
    public ResetPasswordResource {
        if (emailAddress == null || emailAddress.isBlank())
            throw new IllegalArgumentException("Email address cannot be null or blank");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be null or blank");
        if (confirmPassword == null || confirmPassword.isBlank())
            throw new IllegalArgumentException("Confirm password cannot be null or blank");
        if (!password.equals(confirmPassword))
            throw new IllegalArgumentException("Passwords do not match");
    }
}
