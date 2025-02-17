package org.Inventory.DTO;

import lombok.*;
import org.Inventory.Enums.AttributeType;

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
    private boolean required;
    private boolean predefined;
    private List<String> predefinedValues;
    private Set<TemplateInfoDTO> templates;
}