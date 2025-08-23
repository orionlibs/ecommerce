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

public abstract class GeneratedOrderTemplateToOrderCronJob extends CronJob
{
    public static final String ORDERTEMPLATE = "orderTemplate";
    protected static final BidirectionalOneToManyHandler<GeneratedOrderTemplateToOrderCronJob> ORDERTEMPLATEHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERTEMPLATETOORDERCRONJOB, false, "orderTemplate", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("orderTemplate", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ORDERTEMPLATEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Order getOrderTemplate(SessionContext ctx)
    {
        return (Order)getProperty(ctx, "orderTemplate");
    }


    public Order getOrderTemplate()
    {
        return getOrderTemplate(getSession().getSessionContext());
    }


    public void setOrderTemplate(SessionContext ctx, Order value)
    {
        ORDERTEMPLATEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setOrderTemplate(Order value)
    {
        setOrderTemplate(getSession().getSessionContext(), value);
    }
}
