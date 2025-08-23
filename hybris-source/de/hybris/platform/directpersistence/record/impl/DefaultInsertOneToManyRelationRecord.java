package de.hybris.platform.directpersistence.record.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.InsertOneToManyRelationRecord;

public class DefaultInsertOneToManyRelationRecord extends DefaultInsertManyToManyRelationRecord implements InsertOneToManyRelationRecord
{
    private boolean forProcessing = true;


    public DefaultInsertOneToManyRelationRecord(PK sourcePk, PK targetPk)
    {
        super(sourcePk, targetPk, true);
    }


    public void markAsProcessed()
    {
        this.forProcessing = false;
    }


    public boolean isForProcessing()
    {
        return this.forProcessing;
    }
}
