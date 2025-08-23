package com.hybris.datahub.api.event;

import com.hybris.datahub.model.CanonicalItem;
import javax.annotation.concurrent.Immutable;

@Immutable
public class ArchivedCanonicalItemEvent extends BaseCanonicalItemEvent
{
    public ArchivedCanonicalItemEvent(CanonicalItem canonicalItem)
    {
        super(canonicalItem);
    }


    public String toString()
    {
        return "ArchivedCanonicalItemEvent{canonicalItem=" + getCanonicalItem() + "}";
    }
}
