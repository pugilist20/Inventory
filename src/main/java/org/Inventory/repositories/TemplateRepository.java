package org.Inventory.repositories;

import org.Inventory.models.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TemplateRepository extends JpaRepository<Template, UUID> {
}

