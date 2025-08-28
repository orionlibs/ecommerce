package io.github.orionlibs.ecommerce.core.api.idempotency.model;

import io.github.orionlibs.ecommerce.core.database.OrionModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "idempotency_records",
                uniqueConstraints = @UniqueConstraint(
                                name = "uk_idempotency_key_endpoint",
                                columnNames = {"idempotencyKey", "endpoint"}
                ))
@Getter
@Setter
public class IdempotencyRecordModel implements OrionModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String idempotencyKey;
    @Column(nullable = false)
    private String endpoint;
    @Column
    private String requestHash; // Hash of request body to ensure same request
    @Column
    private Integer responseStatus;
    @Column(columnDefinition = "TEXT")
    private String responseBody;
    @Column(columnDefinition = "TEXT")
    private String responseHeaders; // JSON string of headers
    @Column
    private LocalDateTime expiresAt;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    public IdempotencyRecordModel()
    {
    }


    public IdempotencyRecordModel(String idempotencyKey, String endpoint, String requestHash)
    {
        this.idempotencyKey = idempotencyKey;
        this.endpoint = endpoint;
        this.requestHash = requestHash;
        this.expiresAt = LocalDateTime.now().plusHours(24); // Expire after 24 hours
    }
}
