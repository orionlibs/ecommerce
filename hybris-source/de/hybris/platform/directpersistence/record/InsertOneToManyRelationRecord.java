package de.hybris.platform.directpersistence.record;

public interface InsertOneToManyRelationRecord extends InsertManyToManyRelationRecord
{
    void markAsProcessed();


    boolean isForProcessing();
}
