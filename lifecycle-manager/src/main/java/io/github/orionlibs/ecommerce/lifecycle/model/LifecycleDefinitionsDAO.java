package io.github.orionlibs.ecommerce.lifecycle.model;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LifecycleDefinitionsDAO extends JpaRepository<LifecycleDefinitionModel, UUID>
{
    @Query(value = "SELECT 1", nativeQuery = true)
    Integer testConnection();


    Optional<LifecycleDefinitionModel> findByDefinitionKeyAndVersion(String key, int version);
}
