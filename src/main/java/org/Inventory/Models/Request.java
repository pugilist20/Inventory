package org.Inventory.Models;

import jakarta.persistence.*;
import lombok.*;
import org.Inventory.Enums.RequestStatus;

import java.util.UUID;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouseID;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}

