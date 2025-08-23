package de.hybris.platform.orderscheduling.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOrderScheduleCronJob extends CronJob
{
    public static final String ORDER = "order";
    protected static final BidirectionalOneToManyHandler<GeneratedOrderScheduleCronJob> ORDERHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERSCHEDULECRONJOB, false, "order", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("order", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ORDERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Order getOrder(SessionContext ctx)
    {
        return (Order)getProperty(ctx, "order");
    }


    public Order getOrder()
    {
        return getOrder(getSession().getSessionContext());
    }


    public void setOrder(SessionContext ctx, Order value)
    {
        ORDERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setOrder(Order value)
    {
        setOrder(getSession().getSessionContext(), value);
    }
}
