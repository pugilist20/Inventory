package org.Inventory.DTO;

import lombok.Getter;
import lombok.Setter;
import org.Inventory.Enums.AttributeType;
import org.Inventory.Models.Template;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class AttributeRequest {
    private String name;
    private AttributeType type;
    private boolean required;
    private boolean predefined;
    private List<String> predefinedValues;
    private Set<Template> templates;
}
