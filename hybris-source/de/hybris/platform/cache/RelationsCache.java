package de.hybris.platform.cache;

import de.hybris.platform.cache.relation.DefaultRelationsCache;
import de.hybris.platform.cache.relation.RelationAttributes;
import de.hybris.platform.cache.relation.TypeId;
import java.util.Collection;

public interface RelationsCache
{
    static RelationsCache getDefaultInstance()
    {
        return DefaultRelationsCache.instance();
    }


    InvalidationListener createInvalidationListener();


    RelationAttributes getSingleCacheableAttributes(TypeId paramTypeId);


    Collection<RelationAttributes> getAllCacheableAttributes(TypeId paramTypeId);
}
