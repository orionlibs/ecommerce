package de.hybris.platform.directpersistence;

import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.RelationChanges;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ChangeSet
{
    Collection<EntityRecord> getEntityRecords();


    Map<String, RelationChanges> getRelationChanges();


    boolean isJaloWayRecommended();


    ChangeSet withAdded(ChangeSet paramChangeSet);


    default boolean allowEmptyUpdate()
    {
        return true;
    }


    default Collection<PersistResult> getResultsToInvalidate(Set<PersistResult> results)
    {
        return results;
    }
}
