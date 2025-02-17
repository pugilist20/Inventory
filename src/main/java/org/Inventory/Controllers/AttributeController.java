package org.Inventory.Controllers;

import lombok.RequiredArgsConstructor;
import org.Inventory.DTO.AttributeDTO;
import org.Inventory.DTO.AttributeRequest;
import org.Inventory.Models.Attribute;
import org.Inventory.Services.AttributeService;
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
    public ResponseEntity<Attribute> createAttribute(@RequestBody AttributeRequest request) {
        return ResponseEntity.ok(attributeService.createAttribute(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Attribute> updateAttribute(@PathVariable UUID id, @RequestBody AttributeRequest request) {
        return ResponseEntity.ok(attributeService.updateAttribute(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteAttribute(@PathVariable UUID id) {
        attributeService.deleteAttribute(id);
        return ResponseEntity.noContent().build();
    }
}

