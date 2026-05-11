package com.nistra.demy.platform.shared.domain.exceptions;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {
    private final Object[] args;

    protected DomainException(String exception, Object... args) {
        super(exception);
        this.args = args;
    }
}
