package de.hybris.platform.test;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

public class MySpringUnit extends MyUnit
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        allAttributes.put("code", "<" + allAttributes.get("code") + ">");
        return super.createItem(ctx, type, allAttributes);
    }
}
