package org.Inventory.dto;

import lombok.Getter;
import lombok.Setter;
import org.Inventory.enums.RequestStatus;

import java.util.UUID;


@Getter
@Setter
public class ReqRequestDTO {
    private UUID itemID;
    private UUID userID;
    private RequestStatus status;
    private UUID warehouseID;
}
