package org.Inventory.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    MANAGER,
    WORKER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
