package org.Inventory.repositories;

import org.Inventory.models.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttributeRepository extends JpaRepository<Attribute, UUID> {
}

