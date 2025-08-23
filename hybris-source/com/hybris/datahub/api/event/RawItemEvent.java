package com.hybris.datahub.api.event;

import com.google.common.base.Preconditions;
import com.hybris.datahub.model.RawItem;
import javax.annotation.concurrent.Immutable;

@Immutable
public class RawItemEvent extends ItemLevelEvent
{
    private static final long serialVersionUID = -7255984234090418876L;
    private final RawItem rawItem;


    public RawItemEvent(RawItem rawItem)
    {
        Preconditions.checkArgument((rawItem != null), "The raw item is required for creating the event.");
        this.rawItem = rawItem;
    }


    public RawItem getRawItem()
    {
        return this.rawItem;
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
        RawItemEvent that = (RawItemEvent)o;
        if((this.rawItem != null) ? !this.rawItem.equals(that.rawItem) : (that.rawItem != null))
        {
            return false;
        }
        return true;
    }


    public String toString()
    {
        return "RawItemEvent{rawItem=" + this.rawItem + "}";
    }


    public int hashCode()
    {
        return (this.rawItem != null) ? this.rawItem.hashCode() : 0;
    }
}
