package org.Inventory.Repositories;

import org.Inventory.Models.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttributeRepository extends JpaRepository<Attribute, UUID> {
}

