package org.Inventory.exceptions;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int errorCode;
    private final String message;

    public ErrorResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
