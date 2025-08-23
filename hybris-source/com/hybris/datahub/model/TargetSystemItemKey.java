package com.hybris.datahub.model;

import com.hybris.datahub.runtime.domain.TargetSystemPublication;
import javax.annotation.concurrent.Immutable;
import javax.validation.constraints.NotNull;

@Immutable
public class TargetSystemItemKey
{
    private final String targetItemType;
    private final TargetSystemPublication publication;


    public TargetSystemItemKey(@NotNull TargetItem item)
    {
        this(item.getType(), item.getTargetSystemPublication());
    }


    public TargetSystemItemKey(@NotNull String type, @NotNull TargetSystemPublication p)
    {
        this.publication = p;
        this.targetItemType = type;
    }


    public TargetSystemPublication getPublication()
    {
        return this.publication;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o != null && getClass() == o.getClass())
        {
            TargetSystemItemKey that = (TargetSystemItemKey)o;
            return (this.targetItemType.equals(that.targetItemType) && this.publication.equals(that.publication));
        }
        return false;
    }


    public int hashCode()
    {
        return (this.targetItemType + "@" + this.targetItemType).hashCode();
    }


    public String toString()
    {
        return "TargetSystemItemKey{" + this.targetItemType + " in TargetSystemPublication{" + this.publication
                        .getPublicationId() + "}}";
    }
}
