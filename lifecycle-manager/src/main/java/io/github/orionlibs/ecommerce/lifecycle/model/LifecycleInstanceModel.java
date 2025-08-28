package io.github.orionlibs.ecommerce.lifecycle.model;

import io.github.orionlibs.ecommerce.core.database.OrionModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "lifecycle_instances", schema = "uns", indexes = {
                @Index(name = "idx_uns_lifecycle_instances", columnList = "id")
})
@Getter
@Setter
public class LifecycleInstanceModel implements OrionModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String definitionKey;
    @Column(nullable = false)
    private int definitionVersion;
    @Column(nullable = false)
    private String currentState;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
