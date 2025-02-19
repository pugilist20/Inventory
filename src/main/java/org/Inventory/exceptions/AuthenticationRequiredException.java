package org.Inventory.exceptions;

public class AuthenticationRequiredException extends ApiException {
    public AuthenticationRequiredException(String message) {
        super(message, 401);
    }
}
