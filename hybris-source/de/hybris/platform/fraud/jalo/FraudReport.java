package de.hybris.platform.fraud.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Date;

public class FraudReport extends GeneratedFraudReport
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Order owningOrder = (Order)allAttributes.get("order");
        if(owningOrder == null)
        {
            throw new JaloInvalidParameterException("Missing order for creating a new " + type.getCode(), 0);
        }
        if(allAttributes.get("timestamp") == null)
        {
            allAttributes.put("timestamp", new Date());
        }
        allAttributes.setAttributeMode("order", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("timestamp", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("explanation", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("status", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }
}
