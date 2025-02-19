package org.Inventory.exceptions;

public class EntityInUseException extends ApiException {
    public EntityInUseException(String entityName, String dependency) {
        super(entityName + " cannot be deleted: used by " + dependency, 400);
    }
}
