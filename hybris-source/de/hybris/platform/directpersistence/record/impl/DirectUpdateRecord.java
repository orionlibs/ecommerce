package de.hybris.platform.directpersistence.record.impl;

import de.hybris.platform.core.PK;
import java.util.Set;

public class DirectUpdateRecord extends UpdateRecord
{
    public DirectUpdateRecord(PK pk, String type, long version, Set<PropertyHolder> changes)
    {
        super(pk, type, version, changes);
    }


    public boolean getIncrementOptimisticLockCounter()
    {
        return false;
    }
}
