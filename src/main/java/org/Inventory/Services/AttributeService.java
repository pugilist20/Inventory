package org.Inventory.Services;

import lombok.RequiredArgsConstructor;
import org.Inventory.DTO.AttributeDTO;
import org.Inventory.DTO.AttributeRequest;
import org.Inventory.DTO.TemplateInfoDTO;
import org.Inventory.Models.Attribute;
import org.Inventory.Repositories.AttributeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeService {

    private final AttributeRepository attributeRepository;

    public List<AttributeDTO> getAllAttributes() {
        return attributeRepository.findAll().stream()
                .map(attribute -> {
                    Set<TemplateInfoDTO> templateInfoDTOs = attribute.getTemplates().stream()
                            .map(template -> new TemplateInfoDTO(template.getId(), template.getName()))
                            .collect(Collectors.toSet());

                    return AttributeDTO.builder()
                            .id(attribute.getId())
                            .name(attribute.getName())
                            .type(attribute.getType())
                            .required(attribute.isRequired())
                            .predefined(attribute.isPredefined())
                            .predefinedValues(attribute.getPredefinedValues())
                            .templates(templateInfoDTOs)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public AttributeDTO getAttributeById(UUID id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        Set<TemplateInfoDTO> templateInfoDTOs = attribute.getTemplates().stream()
                .map(template -> new TemplateInfoDTO(template.getId(), template.getName()))
                .collect(Collectors.toSet());

        return AttributeDTO.builder()
                .id(attribute.getId())
                .name(attribute.getName())
                .type(attribute.getType())
                .required(attribute.isRequired())
                .predefined(attribute.isPredefined())
                .predefinedValues(attribute.getPredefinedValues())
                .templates(templateInfoDTOs)
                .build();
    }

    public Attribute createAttribute(AttributeRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Attribute name cannot be empty");
        }
        if (request.getType() == null) {
            throw new IllegalArgumentException("Attribute type must be specified");
        }

        Attribute newAttribute = Attribute.builder()
                .name(request.getName())
                .type(request.getType())
                .required(request.isRequired())
                .predefined(request.isPredefined())
                .predefinedValues(request.getPredefinedValues())
                .templates(request.getTemplates())
                .build();

        return attributeRepository.save(newAttribute);
    }

    public Attribute updateAttribute(UUID id, AttributeRequest request) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        attribute.setName(request.getName());
        attribute.setType(request.getType());
        attribute.setRequired(request.isRequired());
        attribute.setPredefined(request.isPredefined());
        attribute.setPredefinedValues(request.getPredefinedValues());

        return attributeRepository.save(attribute);
    }

    public void deleteAttribute(UUID id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        if (!attribute.getTemplates().isEmpty()) {
            throw new IllegalStateException("Cannot delete attribute: used in templates");
        }

        attributeRepository.deleteById(id);
    }
}
