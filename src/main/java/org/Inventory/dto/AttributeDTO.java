package org.Inventory.dto;

import lombok.*;
import org.Inventory.enums.AttributeType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeDTO {
    private UUID id;
    private String name;
    private AttributeType type;
    private Boolean required;
    private Boolean predefined;
    private List<String> predefinedValues;
    private Set<TemplateInfoDTO> templates;
}