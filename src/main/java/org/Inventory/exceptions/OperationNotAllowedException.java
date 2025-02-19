package org.Inventory.exceptions;

public class OperationNotAllowedException extends ApiException {
    public OperationNotAllowedException(String message) {
        super(message, 400);
    }
}
