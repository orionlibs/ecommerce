package de.hybris.platform.directpersistence.record.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.RemoveOneToManyRelationsRecord;

public class DefaultRemoveOneToManyRelationsRecord extends DefaultInsertOneToManyRelationRecord implements RemoveOneToManyRelationsRecord
{
    public DefaultRemoveOneToManyRelationsRecord(PK sourcePk)
    {
        super(sourcePk, null);
    }
}
