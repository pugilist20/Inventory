package org.Inventory.DTO;

import lombok.Getter;
import lombok.Setter;
import org.Inventory.Models.User;

import java.util.UUID;

@Getter
@Setter
public class WarehouseRequest {
    private UUID id;
    private String name;
    private UUID userID;
}
