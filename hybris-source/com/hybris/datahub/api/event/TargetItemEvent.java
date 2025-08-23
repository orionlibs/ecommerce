package com.hybris.datahub.api.event;

import com.google.common.base.Preconditions;
import com.hybris.datahub.model.TargetItem;
import javax.annotation.concurrent.Immutable;

@Immutable
public class TargetItemEvent extends ItemLevelEvent
{
    private static final long serialVersionUID = -7255984234090418876L;
    private final TargetItem targetItem;


    public TargetItemEvent(TargetItem targetItem)
    {
        Preconditions.checkArgument((targetItem != null), "The target item is required for creating the event.");
        this.targetItem = targetItem;
    }


    public TargetItem getTargetItem()
    {
        return this.targetItem;
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
        TargetItemEvent that = (TargetItemEvent)o;
        if((this.targetItem != null) ? !this.targetItem.equals(that.targetItem) : (that.targetItem != null))
        {
            return false;
        }
        return true;
    }


    public String toString()
    {
        return "TargetItemEvent{targetItem=" + this.targetItem + "}";
    }


    public int hashCode()
    {
        return (this.targetItem != null) ? this.targetItem.hashCode() : 0;
    }
}
