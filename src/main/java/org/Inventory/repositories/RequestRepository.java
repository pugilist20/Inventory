package org.Inventory.repositories;

import org.Inventory.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
    List<Request> findByUserId(UUID userId);

    List<Request> findByItemId(UUID itemId);
}

