package org.Inventory.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ItemRequest {
    private String name;
    private UUID templateID;
    private UUID warehouseID;
}
