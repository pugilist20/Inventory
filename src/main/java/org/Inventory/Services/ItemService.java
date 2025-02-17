package org.Inventory.Services;

import org.Inventory.DTO.ItemRequest;
import org.Inventory.Enums.Role;
import org.Inventory.Models.Item;
import org.Inventory.Models.Template;
import org.Inventory.Models.User;
import org.Inventory.Models.Warehouse;
import org.Inventory.Repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.Inventory.Repositories.RequestRepository;
import org.Inventory.Repositories.TemplateRepository;
import org.Inventory.Repositories.WarehouseRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final TemplateRepository templateRepository;
    private final WarehouseRepository warehouseRepository;
    private final RequestRepository requestRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Item> getAllItems() {
        System.out.println(passwordEncoder.encode("123"));
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(UUID id) {
        return itemRepository.findById(id);
    }

    public List<Item> getItemsByWarehouse(UUID warehouseId) {
        return itemRepository.findByWarehouseId(warehouseId);
    }

    public Item createItem(ItemRequest item) {
        Template template = templateRepository.findById(item.getTemplateID())
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        Warehouse warehouse = null;
        if (item.getWarehouseID() != null) {
            warehouse = warehouseRepository.findById(item.getWarehouseID())
                    .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));

            User currentUser = getCurrentUser();
            if (!isManager(currentUser) && !warehouse.getResponsibleUser().getId().equals(currentUser.getId())) {
                throw new SecurityException("Forbidden: You are not authorized to add items to this warehouse");
            }
        }

        Item newItem = new Item();
        newItem.setName(item.getName());
        newItem.setTemplate(template);
        newItem.setWarehouse(warehouse);
        return itemRepository.save(newItem);
    }



    public Item updateItem(UUID id, ItemRequest updatedItem) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Template template = templateRepository.findById(updatedItem.getTemplateID())
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));
        Warehouse warehouse = null;
        if (updatedItem.getWarehouseID() != null) {
            warehouseRepository.findById(updatedItem.getWarehouseID())
                    .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));
        }

        item.setName(updatedItem.getName());
        item.setTemplate(template);
        item.setWarehouse(warehouse);
        return itemRepository.save(item);
    }

    public void deleteItem(UUID id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getWarehouse() != null) {
            User currentUser = getCurrentUser();
            if (!isManager(currentUser) && !item.getWarehouse().getResponsibleUser().getId().equals(currentUser.getId())) {
                throw new SecurityException("Forbidden: You are not authorized to delete items from this warehouse");
            }
        }

        boolean hasPendingRequests = requestRepository.findByItemId(id)
                .stream()
                .anyMatch(request -> "PENDING".equals(request.getStatus()));

        if (hasPendingRequests) {
            throw new IllegalStateException("Cannot delete item: has pending requests");
        }

        itemRepository.deleteById(id);
    }



    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user;
        }
        throw new IllegalStateException("User is not authenticated");
    }


    private boolean isManager(User user) {
        return user.getRoles().contains(Role.MANAGER);
    }
}

