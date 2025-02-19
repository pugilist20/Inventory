package org.Inventory.exceptions;

public class EntityNotFoundException extends ApiException {
    public EntityNotFoundException(String entityName) {
        super(entityName+" not found", 404);
    }
}

