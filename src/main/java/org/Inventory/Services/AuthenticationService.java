package org.Inventory.Services;

import lombok.RequiredArgsConstructor;
import org.Inventory.DTO.LoginRequest;
import org.Inventory.DTO.UserRegistrationRequest;
import org.Inventory.Enums.Role;
import org.Inventory.Models.User;
import org.Inventory.Repositories.UserRepository;
import org.Inventory.Utils.PasswordGenerator;
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

    public void register(UserRegistrationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email уже используется");
        }
        if (userRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Логин уже используется");
        }

        String generatedPassword = PasswordGenerator.generateRandomPassword(12);

        Set<Role> roles = new HashSet<>();
        for (String roleName : request.getRoles()) {
            try {
                roles.add(Role.valueOf(roleName));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Некорректная роль: " + roleName);
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
                "Регистрация в системе учета инструментов",
                "Ваш аккаунт успешно создан.\nЛогин: " + user.getLogin() + "\nПароль: " + generatedPassword
        );
    }


    public String login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        User user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new IllegalArgumentException("Неверный логин или пароль"));
        return jwtService.generateToken(user);
    }
}