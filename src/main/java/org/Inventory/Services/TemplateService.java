package org.Inventory.Services;

import org.Inventory.DTO.TemplateRequest;
import org.Inventory.Models.Template;
import org.Inventory.Repositories.ItemRepository;
import org.Inventory.Repositories.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final ItemRepository itemRepository;

    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    public Optional<Template> getTemplateById(UUID id) {
        return templateRepository.findById(id);
    }

    public Template createTemplate(TemplateRequest template) {
        if (template.getAttributes() == null || template.getAttributes().isEmpty()) {
            throw new IllegalArgumentException("Template must have at least one attribute");
        }
        Template newTemplate = new Template();
        newTemplate.setName(template.getName());
        newTemplate.setAttributes(template.getAttributes());
        return templateRepository.save(newTemplate);
    }

    public Template updateTemplate(UUID id, TemplateRequest updatedTemplate) {
        return templateRepository.findById(id).map(template -> {
            template.setName(updatedTemplate.getName());
            return templateRepository.save(template);
        }).orElseThrow(() -> new RuntimeException("Template not found"));
    }

    public void deleteTemplate(UUID id) {
        boolean isUsed = itemRepository.findAll()
                .stream()
                .anyMatch(item -> item.getTemplate().getId().equals(id));

        if (isUsed) {
            throw new IllegalStateException("Cannot delete template: used by items");
        }

        templateRepository.deleteById(id);
    }
}

