package de.hybris.platform.directpersistence;

import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.RelationChanges;
import java.util.Collection;

public interface MutableChangeSet extends ChangeSet
{
    void add(EntityRecord... paramVarArgs);


    void addAllEntityRecords(Collection<? extends EntityRecord> paramCollection);


    void putRelationChanges(String paramString, RelationChanges paramRelationChanges);


    RelationChanges getRelationChangesForRelation(String paramString);


    void sortEntityRecords();


    void sortAll();


    void groupOrderInformation();


    void setJaloWayRecommended(boolean paramBoolean);


    void finish();
}
