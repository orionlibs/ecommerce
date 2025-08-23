package de.hybris.platform.orderhistory.jalo;

import de.hybris.platform.basecommerce.jalo.BasecommerceManager;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OrderHistoryEntry extends GeneratedOrderHistoryEntry
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Order owningOrder = (Order)allAttributes.get("order");
        if(owningOrder == null)
        {
            throw new JaloInvalidParameterException("Missing order for creating a new " + type.getCode(), 0);
        }
        Order prev = (Order)allAttributes.get("previousOrderVersion");
        if(prev != null && BasecommerceManager.getInstance().getVersionID(prev) == null)
        {
            throw new JaloInvalidParameterException("Illegal previous order version " + prev + " order is no copy!", 0);
        }
        if(allAttributes.get("timestamp") == null)
        {
            allAttributes.put("timestamp", new Date());
        }
        if(allAttributes.get("orderPOS") == null)
        {
            allAttributes.put("orderPOS", Integer.valueOf(queryNewPos(owningOrder)));
        }
        allAttributes.setAttributeMode("order", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("orderPOS", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("timestamp", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("description", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("previousOrderVersion", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    protected int queryNewPos(Order owningOrder)
    {
        List<Integer> ret = FlexibleSearch.getInstance().search("SELECT MAX({orderPOS}) FROM {OrderHistoryEntry} WHERE {order}=?o ", Collections.singletonMap("o", owningOrder), Integer.class).getResult();
        return (ret.isEmpty() || ret.get(0) == null) ? 0 : (((Integer)ret.get(0)).intValue() + 1);
    }


    public void setPreviousOrderVersion(SessionContext ctx, Order prev)
    {
        if(prev != null && BasecommerceManager.getInstance().getVersionID(prev) == null)
        {
            throw new JaloInvalidParameterException("Illegal previous order version " + prev + " order is no copy!", 0);
        }
        super.setPreviousOrderVersion(ctx, prev);
    }
}
