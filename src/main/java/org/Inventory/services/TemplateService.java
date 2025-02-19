package org.Inventory.services;

import org.Inventory.dto.TemplateRequestDTO;
import org.Inventory.exceptions.EntityInUseException;
import org.Inventory.exceptions.EntityNotFoundException;
import org.Inventory.exceptions.InvalidRequestException;
import org.Inventory.models.Template;
import org.Inventory.repositories.ItemRepository;
import org.Inventory.repositories.TemplateRepository;
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

    public Template createTemplate(TemplateRequestDTO template) {
        if (template.getAttributes() == null || template.getAttributes().isEmpty()) {
            throw new InvalidRequestException("Template must have at least one attribute");
        }
        Template newTemplate = new Template();
        newTemplate.setName(template.getName());
        newTemplate.setAttributes(template.getAttributes());
        return templateRepository.save(newTemplate);
    }

    public Template updateTemplate(UUID id, TemplateRequestDTO updatedTemplate) {
        return templateRepository.findById(id).map(template -> {
            template.setName(updatedTemplate.getName());
            return templateRepository.save(template);
        }).orElseThrow(() -> new EntityNotFoundException("Template"));
    }

    public void deleteTemplate(UUID id) {
        boolean isUsed = itemRepository.findAll()
                .stream()
                .anyMatch(item -> item.getTemplate().getId().equals(id));

        if (isUsed) {
            throw new EntityInUseException("Template", "items");
        }

        templateRepository.deleteById(id);
    }
}

