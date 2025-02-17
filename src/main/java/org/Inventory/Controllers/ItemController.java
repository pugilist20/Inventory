package org.Inventory.Controllers;

import lombok.RequiredArgsConstructor;
import org.Inventory.DTO.ItemRequest;
import org.Inventory.Models.Item;
import org.Inventory.Services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Item>> getItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Item> getItemById(@PathVariable UUID id) {
        Optional<Item> item = itemService.getItemById(id);
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Item>> getItemsByWarehouse(@PathVariable UUID warehouseId) {
        List<Item> items = itemService.getItemsByWarehouse(warehouseId);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody ItemRequest request) {
        return ResponseEntity.ok(itemService.createItem(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Item> updateItem(@PathVariable UUID id, @RequestBody ItemRequest request) {
        return ResponseEntity.ok(itemService.updateItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}

