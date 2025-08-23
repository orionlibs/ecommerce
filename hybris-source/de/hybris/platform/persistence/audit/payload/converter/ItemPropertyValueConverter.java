package de.hybris.platform.persistence.audit.payload.converter;

import de.hybris.platform.core.PK;
import de.hybris.platform.util.ItemPropertyValue;

public class ItemPropertyValueConverter implements PayloadConverter<ItemPropertyValue>
{
    public String convertToString(ItemPropertyValue obj)
    {
        return obj.getPK().getLongValueAsString();
    }


    public ItemPropertyValue convertFromString(String str)
    {
        return new ItemPropertyValue(PK.parse(str));
    }


    public Class<ItemPropertyValue> forClass()
    {
        return ItemPropertyValue.class;
    }
}
