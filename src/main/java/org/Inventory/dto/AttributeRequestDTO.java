package org.Inventory.dto;

import lombok.Getter;
import lombok.Setter;
import org.Inventory.enums.AttributeType;
import org.Inventory.models.Template;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class AttributeRequestDTO {
    private String name;
    private AttributeType type;
    private boolean required;
    private boolean predefined;
    private List<String> predefinedValues;
    private Set<Template> templates;
}
