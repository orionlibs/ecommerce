package de.hybris.platform.persistence.audit.payload.converter;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;

public class OldJaloItemConverter implements PayloadConverter<Item>
{
    public String convertToString(Item obj)
    {
        return obj.getPK().getLongValueAsString();
    }


    public Item convertFromString(String str)
    {
        return JaloSession.getCurrentSession().getItem(PK.parse(str));
    }


    public Class<Item> forClass()
    {
        return Item.class;
    }
}
