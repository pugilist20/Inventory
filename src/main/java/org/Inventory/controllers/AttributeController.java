package org.Inventory.controllers;

import lombok.RequiredArgsConstructor;
import org.Inventory.dto.AttributeDTO;
import org.Inventory.dto.AttributeRequestDTO;
import org.Inventory.models.Attribute;
import org.Inventory.services.AttributeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attributes")
@RequiredArgsConstructor
public class AttributeController {

    private final AttributeService attributeService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AttributeDTO>> getAttributes() {
        List<AttributeDTO> attributeDTOS=attributeService.getAllAttributes();
        return ResponseEntity.ok(attributeDTOS);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AttributeDTO> getAttributeById(@PathVariable UUID id) {
        AttributeDTO attributeDTO = attributeService.getAttributeById(id);
        return ResponseEntity.ok(attributeDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Attribute> createAttribute(@RequestBody AttributeRequestDTO request) {
        return ResponseEntity.ok(attributeService.createAttribute(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Attribute> updateAttribute(@PathVariable UUID id, @RequestBody AttributeRequestDTO request) {
        return ResponseEntity.ok(attributeService.updateAttribute(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteAttribute(@PathVariable UUID id) {
        attributeService.deleteAttribute(id);
        return ResponseEntity.noContent().build();
    }
}

