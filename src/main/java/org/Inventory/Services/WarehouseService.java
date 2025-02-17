package org.Inventory.Services;

import org.Inventory.DTO.WarehouseRequest;
import org.Inventory.Models.User;
import org.Inventory.Models.Warehouse;
import org.Inventory.Repositories.ItemRepository;
import org.Inventory.Repositories.UserRepository;
import org.Inventory.Repositories.WarehouseRepository;
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

    public Warehouse createWarehouse(WarehouseRequest warehouse) {
        User user=userRepository.findById(warehouse.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("Responsible user not found"));
        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setName(warehouse.getName());
        newWarehouse.setResponsibleUser(user);
        return warehouseRepository.save(newWarehouse);
    }

    public Warehouse updateWarehouse(UUID id, WarehouseRequest updatedWarehouse) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        User user=userRepository.findById(updatedWarehouse.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("Responsible user not found"));

        warehouse.setName(updatedWarehouse.getName());
        warehouse.setResponsibleUser(user);
        return warehouseRepository.save(warehouse);
    }

    public void deleteWarehouse(UUID id) {
        boolean hasItems = !itemRepository.findByWarehouseId(id).isEmpty();
        if (hasItems) {
            throw new IllegalStateException("Cannot delete warehouse: contains items");
        }

        warehouseRepository.deleteById(id);
    }
}

