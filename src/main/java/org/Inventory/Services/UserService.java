package org.Inventory.Services;

import org.Inventory.DTO.UserAdminRequest;
import org.Inventory.DTO.UserRequest;
import org.Inventory.Enums.Role;
import org.Inventory.Models.User;
import org.Inventory.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.Inventory.Repositories.WarehouseRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WarehouseRepository warehouseRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public User updateUser(UUID id, UserRequest updatedUser) {
        User user = findUserById(id);
        validateUserUpdates(user, updatedUser.getEmail(), updatedUser.getLogin());
        updateUserFields(user, updatedUser.getEmail(), updatedUser.getLogin());
        return userRepository.save(user);
    }

    public User updateUserAdmin(UUID id, UserAdminRequest updatedUser) {
        User user = findUserById(id);
        validateUserUpdates(user, updatedUser.getEmail(), updatedUser.getLogin());
        updateUserFields(user, updatedUser.getEmail(), updatedUser.getLogin(), updatedUser.getRoles());
        return userRepository.save(user);
    }

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void validateUserUpdates(User user, String newEmail, String newLogin) {
        if (!user.getEmail().equals(newEmail) && userRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (!user.getLogin().equals(newLogin) && userRepository.findByLogin(newLogin).isPresent()) {
            throw new IllegalArgumentException("Login already in use");
        }
    }

    private void updateUserFields(User user, String email, String login, Set<Role> roles) {
        user.setEmail(email);
        user.setLogin(login);
        user.setRoles(roles);
    }

    private void updateUserFields(User user, String email, String login) {
        updateUserFields(user, email, login, user.getRoles());
    }

    public void deleteUser(UUID id) {
        boolean isResponsible = warehouseRepository.findAll()
                .stream()
                .anyMatch(warehouse -> warehouse.getResponsibleUser().getId().equals(id));

        if (isResponsible) {
            throw new IllegalStateException("Cannot delete user: assigned as responsible for a warehouse");
        }

        userRepository.deleteById(id);
    }
}

