package de.hybris.platform.ordercancel.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.ComposedType;

public class OrderCancelRecord extends GeneratedOrderCancelRecord
{
    private static final String SUFFIX = "_CANCEL";


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Order owningOrder = (Order)allAttributes.get("order");
        allAttributes.put("identifier", "" + owningOrder + "_CANCEL");
        allAttributes.setAttributeMode("identifier", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }
}
