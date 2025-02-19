package org.Inventory.services;

import org.Inventory.dto.WarehouseRequestDTO;
import org.Inventory.exceptions.EntityInUseException;
import org.Inventory.exceptions.EntityNotFoundException;
import org.Inventory.models.User;
import org.Inventory.models.Warehouse;
import org.Inventory.repositories.ItemRepository;
import org.Inventory.repositories.UserRepository;
import org.Inventory.repositories.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Optional<Warehouse> getWarehouseById(UUID id) {
        return warehouseRepository.findById(id);
    }

    public Warehouse createWarehouse(WarehouseRequestDTO warehouse) {
        User user = userRepository.findById(warehouse.getUserID())
                .orElseThrow(() -> new EntityNotFoundException("Responsible user not found"));
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setName(warehouse.getName());
        newWarehouse.setResponsibleUser(user);
        return warehouseRepository.save(newWarehouse);
    }

    public Warehouse updateWarehouse(UUID id, WarehouseRequestDTO updatedWarehouse) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));

        User user = userRepository.findById(updatedWarehouse.getUserID())
                .orElseThrow(() -> new EntityNotFoundException("Responsible user not found"));

        warehouse.setName(updatedWarehouse.getName());
        warehouse.setResponsibleUser(user);
        return warehouseRepository.save(warehouse);
    }

    public void deleteWarehouse(UUID id) {
        boolean hasItems = !itemRepository.findByWarehouseId(id).isEmpty();
        if (hasItems) {
            throw new EntityInUseException("Warehouse", "items");
        }

        warehouseRepository.deleteById(id);
    }
}

