package org.Inventory.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.Inventory.enums.AttributeType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "attributes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attribute {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttributeType type;

    private boolean required;
    private boolean predefined;

    @ElementCollection
    private List<String> predefinedValues;

    @ManyToMany(mappedBy = "attributes")
    @JsonBackReference
    private Set<Template> templates;
}
