package com.hybris.datahub.api.event;

import com.hybris.datahub.model.CanonicalItem;
import javax.annotation.concurrent.Immutable;

@Immutable
public class CanonicalItemEvent extends BaseCanonicalItemEvent
{
    public CanonicalItemEvent(CanonicalItem canonicalItem)
    {
        super(canonicalItem);
    }


    public String toString()
    {
        return "CanonicalItemEvent{canonicalItem=" + getCanonicalItem() + "}";
    }
}
