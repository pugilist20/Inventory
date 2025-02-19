package org.Inventory.dto;

import lombok.Getter;
import lombok.Setter;
import org.Inventory.enums.Role;

import java.util.Set;

@Getter
@Setter
public class UserAdminRequestDTO {
    private String email;
    private String login;
    private Set<Role> roles;
}
