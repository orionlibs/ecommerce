package de.hybris.platform.productcockpit.services.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.MappingService;
import de.hybris.platform.core.model.ItemModel;

public class PKKeyProvider implements MappingService.KeyProvider
{
    public Object getKey(Object item) throws IllegalArgumentException
    {
        Object ret = null;
        Object object = null;
        if(item instanceof TypedObject)
        {
            object = ((TypedObject)item).getObject();
            if(object instanceof ItemModel)
            {
                ItemModel realItem = (ItemModel)object;
                ret = realItem.getPk();
            }
        }
        else
        {
            throw new IllegalArgumentException("Can not get key for item '" + item + "'. Expected type: '" + TypedObject.class
                            .getName() + "', Found type: '" + item.getClass().getName() + "'.");
        }
        return ret;
    }
}
