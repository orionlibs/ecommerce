package io.github.orionlibs.ecommerce.lifecycle.model;

import io.github.orionlibs.ecommerce.core.database.OrionModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "lifecycle_definitions", schema = "uns", indexes = {
                @Index(name = "idx_uns_lifecycle_definitions", columnList = "definition_key,version")
},
                uniqueConstraints = @UniqueConstraint(columnNames = {"definition_key", "version"}))
@Getter
@Setter
public class LifecycleDefinitionModel implements OrionModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(name = "definition_key", nullable = false)
    private String key;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column(nullable = false)
    private String payload;
    @Column(nullable = false)
    private int version;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
