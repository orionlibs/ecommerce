package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.commons.lang.math.NumberUtils;

public class ObjectCollectionItemReference extends GeneratedObjectCollectionItemReference
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        return item;
    }


    @Deprecated
    public Integer getPosition(SessionContext ctx)
    {
        return NumberUtils.INTEGER_ZERO;
    }
}
