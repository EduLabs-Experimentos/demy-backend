package com.nistra.demy.platform.iam.domain.exceptions;

import com.nistra.demy.platform.shared.domain.exceptions.DomainException;

public class OldPasswordMismatchException extends DomainException {
    public OldPasswordMismatchException() {
        super("Old password does not match the current password");
    }
}
