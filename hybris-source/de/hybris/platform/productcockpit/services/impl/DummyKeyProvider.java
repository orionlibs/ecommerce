package de.hybris.platform.productcockpit.services.impl;

import de.hybris.platform.cockpit.services.MappingService;

public class DummyKeyProvider implements MappingService.KeyProvider
{
    public Object getKey(Object item) throws IllegalArgumentException
    {
        Object ret = null;
        if(item instanceof de.hybris.platform.cockpit.model.meta.TypedObject)
        {
            ret = item;
        }
        else
        {
            throw new IllegalArgumentException("Item not of type 'TypedObject'.");
        }
        return ret;
    }
}
