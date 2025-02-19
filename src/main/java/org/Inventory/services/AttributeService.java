package org.Inventory.services;

import lombok.RequiredArgsConstructor;
import org.Inventory.dto.AttributeDTO;
import org.Inventory.dto.AttributeRequestDTO;
import org.Inventory.dto.TemplateInfoDTO;
import org.Inventory.exceptions.EntityInUseException;
import org.Inventory.exceptions.EntityNotFoundException;
import org.Inventory.exceptions.InvalidRequestException;
import org.Inventory.models.Attribute;
import org.Inventory.repositories.AttributeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .orElseThrow(() -> new EntityNotFoundException("Attribute"));

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

    public Attribute createAttribute(AttributeRequestDTO request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new InvalidRequestException("Attribute name cannot be empty");
        }
        if (request.getType() == null) {
            throw new InvalidRequestException("Attribute type must be specified");
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

    public Attribute updateAttribute(UUID id, AttributeRequestDTO request) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attribute"));

        attribute.setName(request.getName());
        attribute.setType(request.getType());
        attribute.setRequired(request.isRequired());
        attribute.setPredefined(request.isPredefined());
        attribute.setPredefinedValues(request.getPredefinedValues());

        return attributeRepository.save(attribute);
    }

    public void deleteAttribute(UUID id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attribute"));

        if (!attribute.getTemplates().isEmpty()) {
            throw new EntityInUseException("Attribute", "templates");
        }

        attributeRepository.deleteById(id);
    }
}
