package org.Inventory.DTO;

import lombok.Getter;
import lombok.Setter;
import org.Inventory.Enums.Role;

import java.util.Set;

@Getter
@Setter
public class UserAdminRequest {
    private String email;
    private String login;
    private Set<Role> roles;
}
