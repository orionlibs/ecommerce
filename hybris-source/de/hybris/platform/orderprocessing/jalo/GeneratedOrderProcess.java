package de.hybris.platform.orderprocessing.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ordersplitting.jalo.ConsignmentProcess;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOrderProcess extends BusinessProcess
{
    public static final String ORDER = "order";
    public static final String CONSIGNMENTPROCESSES = "consignmentProcesses";
    protected static final BidirectionalOneToManyHandler<GeneratedOrderProcess> ORDERHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERPROCESS, false, "order", null, false, true, 0);
    protected static final OneToManyHandler<ConsignmentProcess> CONSIGNMENTPROCESSESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.CONSIGNMENTPROCESS, false, "parentProcess", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(BusinessProcess.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("order", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<ConsignmentProcess> getConsignmentProcesses(SessionContext ctx)
    {
        return CONSIGNMENTPROCESSESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<ConsignmentProcess> getConsignmentProcesses()
    {
        return getConsignmentProcesses(getSession().getSessionContext());
    }


    public void setConsignmentProcesses(SessionContext ctx, Collection<ConsignmentProcess> value)
    {
        CONSIGNMENTPROCESSESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setConsignmentProcesses(Collection<ConsignmentProcess> value)
    {
        setConsignmentProcesses(getSession().getSessionContext(), value);
    }


    public void addToConsignmentProcesses(SessionContext ctx, ConsignmentProcess value)
    {
        CONSIGNMENTPROCESSESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToConsignmentProcesses(ConsignmentProcess value)
    {
        addToConsignmentProcesses(getSession().getSessionContext(), value);
    }


    public void removeFromConsignmentProcesses(SessionContext ctx, ConsignmentProcess value)
    {
        CONSIGNMENTPROCESSESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromConsignmentProcesses(ConsignmentProcess value)
    {
        removeFromConsignmentProcesses(getSession().getSessionContext(), value);
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
