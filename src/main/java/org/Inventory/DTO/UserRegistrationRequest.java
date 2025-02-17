package org.Inventory.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserRegistrationRequest {
    private String email;
    private String login;
    private Set<String> roles;
}