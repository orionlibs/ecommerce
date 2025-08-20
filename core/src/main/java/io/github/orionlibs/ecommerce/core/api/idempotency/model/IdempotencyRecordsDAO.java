package io.github.orionlibs.ecommerce.core.api.idempotency.model;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyRecordsDAO extends JpaRepository<IdempotencyRecordModel, Long>
{
    @Query(value = "SELECT 1", nativeQuery = true)
    Integer testConnection();


    Optional<IdempotencyRecordModel> findByIdempotencyKeyAndEndpoint(String idempotencyKey, String endpoint);


    @Modifying
    @Query("DELETE FROM IdempotencyRecordModel c WHERE c.expiresAt < :now")
    void deleteExpiredRecords(@Param("now") LocalDateTime now);
}
