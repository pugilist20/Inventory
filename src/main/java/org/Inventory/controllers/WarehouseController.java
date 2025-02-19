package org.Inventory.controllers;

import lombok.RequiredArgsConstructor;
import org.Inventory.dto.WarehouseRequestDTO;
import org.Inventory.models.Warehouse;
import org.Inventory.services.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Warehouse>> getWarehouses() {
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable UUID id) {
        Optional<Warehouse> warehouse = warehouseService.getWarehouseById(id);
        return warehouse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody WarehouseRequestDTO request) {
        return ResponseEntity.ok(warehouseService.createWarehouse(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable UUID id, @RequestBody WarehouseRequestDTO request) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable UUID id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}
