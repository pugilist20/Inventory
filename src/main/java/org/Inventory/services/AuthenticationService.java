package org.Inventory.services;

import lombok.RequiredArgsConstructor;
import org.Inventory.dto.LoginRequestDTO;
import org.Inventory.dto.UserRegistrationRequestDTO;
import org.Inventory.enums.Role;
import org.Inventory.models.User;
import org.Inventory.repositories.UserRepository;
import org.Inventory.security.JwtService;
import org.Inventory.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordGenerator passwordGenerator;
    private final MessageSource messageSource;

    @Value("${spring.jwt.password-length}")
    private Integer passwordLength;

    public void register(UserRegistrationRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException(getMessage("error.email.taken"));
        }
        if (userRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new IllegalArgumentException(getMessage("error.login.taken"));
        }

        String generatedPassword = passwordGenerator.generateRandomPassword(passwordLength);

        Set<Role> roles = new HashSet<>();
        for (String roleName : request.getRoles()) {
            try {
                roles.add(Role.valueOf(roleName));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(getMessage("error.invalid.role", roleName));
            }
        }
        User user = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .login(request.getLogin())
                .password(passwordEncoder.encode(generatedPassword))
                .roles(roles)
                .build();

        userRepository.save(user);

        emailService.sendEmail(
                user.getEmail(),
                getMessage("email.registration.subject"),
                getMessage("email.registration.body", user.getLogin(), generatedPassword)
        );
    }

    public String login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        User user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new IllegalArgumentException(getMessage("error.invalid.credentials")));
        return jwtService.generateToken(user);
    }

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
