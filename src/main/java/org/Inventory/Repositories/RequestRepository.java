package org.Inventory.Repositories;

import org.Inventory.Models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
    List<Request> findByUserId(UUID userId);

    List<Request> findByItemId(UUID itemId);
}

