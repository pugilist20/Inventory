package org.Inventory.exceptions;

import lombok.Getter;

@Getter
public abstract class ApiException extends RuntimeException {
    private final int errorCode;

    public ApiException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}

