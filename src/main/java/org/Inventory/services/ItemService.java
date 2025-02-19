package org.Inventory.services;

import org.Inventory.dto.ItemRequestDTO;
import org.Inventory.enums.RequestStatus;
import org.Inventory.enums.Role;
import org.Inventory.exceptions.AccessDeniedException;
import org.Inventory.exceptions.AuthenticationRequiredException;
import org.Inventory.exceptions.EntityNotFoundException;
import org.Inventory.exceptions.OperationNotAllowedException;
import org.Inventory.models.Item;
import org.Inventory.models.Template;
import org.Inventory.models.User;
import org.Inventory.models.Warehouse;
import org.Inventory.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.Inventory.repositories.RequestRepository;
import org.Inventory.repositories.TemplateRepository;
import org.Inventory.repositories.WarehouseRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(UUID id) {
        return itemRepository.findById(id);
    }

    public List<Item> getItemsByWarehouse(UUID warehouseId) {
        return itemRepository.findByWarehouseId(warehouseId);
    }

    public Item createItem(ItemRequestDTO item) {
        Template template = templateRepository.findById(item.getTemplateID())
                .orElseThrow(() -> new EntityNotFoundException("Template"));

        Warehouse warehouse = null;
        if (item.getWarehouseID() != null) {
            warehouse = warehouseRepository.findById(item.getWarehouseID())
                    .orElseThrow(() -> new EntityNotFoundException("Warehouse"));

            User currentUser = getCurrentUser();
            if (isManager(currentUser) && !warehouse.getResponsibleUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Forbidden: You are not authorized to add items to this warehouse");
            }
        }

        Item newItem = new Item();
        newItem.setName(item.getName());
        newItem.setTemplate(template);
        newItem.setWarehouse(warehouse);
        return itemRepository.save(newItem);
    }


    public Item updateItem(UUID id, ItemRequestDTO updatedItem) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item"));

        Template template = templateRepository.findById(updatedItem.getTemplateID())
                .orElseThrow(() -> new EntityNotFoundException("Template"));
        Warehouse warehouse = null;
        if (updatedItem.getWarehouseID() != null) {
            warehouse = warehouseRepository.findById(updatedItem.getWarehouseID())
                    .orElseThrow(() -> new EntityNotFoundException("Warehouse"));
        }

        item.setName(updatedItem.getName());
        item.setTemplate(template);
        item.setWarehouse(warehouse);
        return itemRepository.save(item);
    }

    public void deleteItem(UUID id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item"));

        if (item.getWarehouse() != null) {
            User currentUser = getCurrentUser();
            if (isManager(currentUser) && !item.getWarehouse().getResponsibleUser().getId().equals(currentUser.getId())) {
                throw new AccessDeniedException("Forbidden: You are not authorized to delete items from this warehouse");
            }
        }

        boolean hasPendingRequests = requestRepository.findByItemId(id)
                .stream()
                .anyMatch(request -> RequestStatus.PENDING.equals(request.getStatus()));

        if (hasPendingRequests) {
            throw new OperationNotAllowedException("Cannot delete item: has pending requests");
        }

        itemRepository.deleteById(id);
    }


    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user;
        }
        throw new AuthenticationRequiredException("User is not authenticated");
    }


    private boolean isManager(User user) {
        return !user.getRoles().contains(Role.MANAGER);
    }
}

