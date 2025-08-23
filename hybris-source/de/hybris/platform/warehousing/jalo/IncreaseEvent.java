package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

public class IncreaseEvent extends GeneratedIncreaseEvent
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        return item;
    }
}
