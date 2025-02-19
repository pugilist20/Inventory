package org.Inventory.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ItemRequestDTO {
    private String name;
    private UUID templateID;
    private UUID warehouseID;
}
