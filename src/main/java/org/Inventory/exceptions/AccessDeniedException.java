package org.Inventory.exceptions;

public class AccessDeniedException extends ApiException {
    public AccessDeniedException(String message) {
        super(message, 403);
    }
}