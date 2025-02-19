package org.Inventory.exceptions;

public class InvalidRequestException extends ApiException {
    public InvalidRequestException(String message) {
        super(message, 400);
    }
}
