package org.Inventory.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class WarehouseRequestDTO {
    private UUID id;
    private String name;
    private UUID userID;
}
