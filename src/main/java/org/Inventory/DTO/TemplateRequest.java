package org.Inventory.DTO;

import lombok.Getter;
import lombok.Setter;
import org.Inventory.Models.Attribute;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TemplateRequest {
    private UUID id;
    private String name;
    private List<Attribute> attributes;
}
