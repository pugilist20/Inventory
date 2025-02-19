package org.Inventory.dto;

import lombok.Getter;
import lombok.Setter;
import org.Inventory.models.Attribute;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TemplateRequestDTO {
    private UUID id;
    private String name;
    private List<Attribute> attributes;
}
