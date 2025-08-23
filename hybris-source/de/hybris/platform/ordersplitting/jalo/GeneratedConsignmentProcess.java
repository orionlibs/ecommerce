package de.hybris.platform.ordersplitting.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.orderprocessing.jalo.OrderProcess;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedConsignmentProcess extends BusinessProcess
{
    public static final String CONSIGNMENT = "consignment";
    public static final String PARENTPROCESS = "parentProcess";
    protected static final BidirectionalOneToManyHandler<GeneratedConsignmentProcess> CONSIGNMENTHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.CONSIGNMENTPROCESS, false, "consignment", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedConsignmentProcess> PARENTPROCESSHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.CONSIGNMENTPROCESS, false, "parentProcess", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(BusinessProcess.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("consignment", Item.AttributeMode.INITIAL);
        tmp.put("parentProcess", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Consignment getConsignment(SessionContext ctx)
    {
        return (Consignment)getProperty(ctx, "consignment");
    }


    public Consignment getConsignment()
    {
        return getConsignment(getSession().getSessionContext());
    }


    public void setConsignment(SessionContext ctx, Consignment value)
    {
        CONSIGNMENTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setConsignment(Consignment value)
    {
        setConsignment(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CONSIGNMENTHANDLER.newInstance(ctx, allAttributes);
        PARENTPROCESSHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public OrderProcess getParentProcess(SessionContext ctx)
    {
        return (OrderProcess)getProperty(ctx, "parentProcess");
    }


    public OrderProcess getParentProcess()
    {
        return getParentProcess(getSession().getSessionContext());
    }


    public void setParentProcess(SessionContext ctx, OrderProcess value)
    {
        PARENTPROCESSHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setParentProcess(OrderProcess value)
    {
        setParentProcess(getSession().getSessionContext(), value);
    }
}
