package com.hybris.datahub.api.event;

import com.google.common.base.Preconditions;
import com.hybris.datahub.runtime.domain.CanonicalItemPublicationStatus;
import javax.annotation.concurrent.Immutable;

@Immutable
public class CanonicalItemPublicationStatusEvent extends ItemLevelEvent
{
    private static final long serialVersionUID = -7255984234090418876L;
    private final CanonicalItemPublicationStatus canonicalItemPublicationStatus;


    public CanonicalItemPublicationStatusEvent(CanonicalItemPublicationStatus canonicalItemPublicationStatus)
    {
        Preconditions.checkArgument((canonicalItemPublicationStatus != null), "The publication status is required for creating the event.");
        this.canonicalItemPublicationStatus = canonicalItemPublicationStatus;
    }


    public CanonicalItemPublicationStatus getCanonicalItemPublicationStatus()
    {
        return this.canonicalItemPublicationStatus;
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
        CanonicalItemPublicationStatusEvent that = (CanonicalItemPublicationStatusEvent)o;
        if((this.canonicalItemPublicationStatus != null) ? !this.canonicalItemPublicationStatus.equals(that.canonicalItemPublicationStatus) : (that.canonicalItemPublicationStatus != null))
        {
            return false;
        }
        return true;
    }


    public String toString()
    {
        return "CanonicalItemPublicationStatusEvent{canonicalItemPublicationStatus=" + this.canonicalItemPublicationStatus + "}";
    }


    public int hashCode()
    {
        return (this.canonicalItemPublicationStatus != null) ? this.canonicalItemPublicationStatus.hashCode() : 0;
    }
}
