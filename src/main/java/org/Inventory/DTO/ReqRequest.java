package org.Inventory.DTO;

import lombok.Getter;
import lombok.Setter;
import org.Inventory.Enums.RequestStatus;

import java.util.UUID;


@Getter
@Setter
public class ReqRequest {
    private UUID itemID;
    private UUID userID;
    private RequestStatus status;
    private UUID warehouseID;
}
