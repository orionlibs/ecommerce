package com.hybris.backoffice.catalogversioneventhandling.impl;

import com.hybris.backoffice.catalogversioneventhandling.AvailableCatalogVersionsTag;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultAvailableCatalogVersionsTag implements AvailableCatalogVersionsTag
{
    private final AtomicReference<UUID> tag = new AtomicReference<>();


    public UUID getTag()
    {
        return this.tag.get();
    }


    public void refresh()
    {
        this.tag.set(UUID.randomUUID());
    }
}
