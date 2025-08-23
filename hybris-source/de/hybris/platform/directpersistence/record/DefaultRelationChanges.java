package de.hybris.platform.directpersistence.record;

import java.util.Collection;

public interface DefaultRelationChanges extends RelationChanges
{
    Collection<InsertManyToManyRelationRecord> getInsertManyToManyRelationRecords();


    Collection<RemoveManyToManyRelationsRecord> getRemoveManyToManyRelationsRecords();


    Collection<InsertOneToManyRelationRecord> getOneToManyRelationRecords();


    Collection<RemoveOneToManyRelationsRecord> getRemoveOneToManyRelationsRecords();


    void add(InsertManyToManyRelationRecord... paramVarArgs);


    void add(RemoveManyToManyRelationsRecord... paramVarArgs);


    void add(RemoveOneToManyRelationsRecord... paramVarArgs);


    void add(InsertOneToManyRelationRecord... paramVarArgs);
}
