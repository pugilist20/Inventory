package org.Inventory.Repositories;

import org.Inventory.Models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
    List<Item> findByWarehouseId(UUID warehouseId);
}

