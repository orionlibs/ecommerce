package de.hybris.platform.directpersistence.record.impl;

import de.hybris.platform.directpersistence.record.RelationRecord;

public abstract class AbstractRelationRecord implements RelationRecord
{
    protected final boolean srcToTgt;


    public AbstractRelationRecord(boolean srcToTgt)
    {
        this.srcToTgt = srcToTgt;
    }


    public boolean isSrcToTgt()
    {
        return this.srcToTgt;
    }


    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.srcToTgt ? 1231 : 1237);
        return result;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        AbstractRelationRecord other = (AbstractRelationRecord)obj;
        if(this.srcToTgt != other.srcToTgt)
        {
            return false;
        }
        return true;
    }
}
