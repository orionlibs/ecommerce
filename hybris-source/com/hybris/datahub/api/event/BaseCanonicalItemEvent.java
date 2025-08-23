package com.hybris.datahub.api.event;

import com.google.common.base.Preconditions;
import com.hybris.datahub.model.CanonicalItem;
import javax.annotation.concurrent.Immutable;

@Immutable
public abstract class BaseCanonicalItemEvent extends ItemLevelEvent
{
    private final CanonicalItem canonicalItem;


    protected BaseCanonicalItemEvent(CanonicalItem canonicalItem)
    {
        Preconditions.checkArgument((canonicalItem != null), "The canonical item is required for creating the event.");
        this.canonicalItem = canonicalItem;
    }


    public CanonicalItem getCanonicalItem()
    {
        return this.canonicalItem;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        BaseCanonicalItemEvent that = (BaseCanonicalItemEvent)o;
        if((this.canonicalItem != null) ? !this.canonicalItem.equals(that.canonicalItem) : (that.canonicalItem != null))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        return (this.canonicalItem != null) ? this.canonicalItem.hashCode() : 0;
    }
}
