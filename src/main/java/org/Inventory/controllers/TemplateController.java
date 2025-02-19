package org.Inventory.controllers;

import lombok.RequiredArgsConstructor;
import org.Inventory.dto.TemplateRequestDTO;
import org.Inventory.models.Template;
import org.Inventory.services.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Template>> getTemplates() {
        return ResponseEntity.ok(templateService.getAllTemplates());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Template> getTemplateById(@PathVariable UUID id) {
        Optional<Template> template = templateService.getTemplateById(id);
        return template.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Template> createTemplate(@RequestBody TemplateRequestDTO request) {
        return ResponseEntity.ok(templateService.createTemplate(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Template> updateTemplate(@PathVariable UUID id, @RequestBody TemplateRequestDTO request) {
        return ResponseEntity.ok(templateService.updateTemplate(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteTemplate(@PathVariable UUID id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}

