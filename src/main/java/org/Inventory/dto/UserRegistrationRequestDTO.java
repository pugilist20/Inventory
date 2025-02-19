package org.Inventory.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserRegistrationRequestDTO {
    private String email;
    private String login;
    private Set<String> roles;
}