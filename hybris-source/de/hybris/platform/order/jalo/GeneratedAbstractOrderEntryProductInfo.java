package de.hybris.platform.order.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractOrderEntryProductInfo extends GenericItem
{
    public static final String PRODUCTINFOSTATUS = "productInfoStatus";
    public static final String CONFIGURATORTYPE = "configuratorType";
    public static final String ORDERENTRYPOS = "orderEntryPOS";
    public static final String ORDERENTRY = "orderEntry";
    protected static final BidirectionalOneToManyHandler<GeneratedAbstractOrderEntryProductInfo> ORDERENTRYHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.ABSTRACTORDERENTRYPRODUCTINFO, false, "orderEntry", "orderEntryPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("productInfoStatus", Item.AttributeMode.INITIAL);
        tmp.put("configuratorType", Item.AttributeMode.INITIAL);
        tmp.put("orderEntryPOS", Item.AttributeMode.INITIAL);
        tmp.put("orderEntry", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getConfiguratorType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "configuratorType");
    }


    public EnumerationValue getConfiguratorType()
    {
        return getConfiguratorType(getSession().getSessionContext());
    }


    protected void setConfiguratorType(SessionContext ctx, EnumerationValue value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'configuratorType' is not changeable", 0);
        }
        setProperty(ctx, "configuratorType", value);
    }


    protected void setConfiguratorType(EnumerationValue value)
    {
        setConfiguratorType(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ORDERENTRYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public AbstractOrderEntry getOrderEntry(SessionContext ctx)
    {
        return (AbstractOrderEntry)getProperty(ctx, "orderEntry");
    }


    public AbstractOrderEntry getOrderEntry()
    {
        return getOrderEntry(getSession().getSessionContext());
    }


    protected void setOrderEntry(SessionContext ctx, AbstractOrderEntry value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'orderEntry' is not changeable", 0);
        }
        ORDERENTRYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setOrderEntry(AbstractOrderEntry value)
    {
        setOrderEntry(getSession().getSessionContext(), value);
    }


    Integer getOrderEntryPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "orderEntryPOS");
    }


    Integer getOrderEntryPOS()
    {
        return getOrderEntryPOS(getSession().getSessionContext());
    }


    int getOrderEntryPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getOrderEntryPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getOrderEntryPOSAsPrimitive()
    {
        return getOrderEntryPOSAsPrimitive(getSession().getSessionContext());
    }


    void setOrderEntryPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "orderEntryPOS", value);
    }


    void setOrderEntryPOS(Integer value)
    {
        setOrderEntryPOS(getSession().getSessionContext(), value);
    }


    void setOrderEntryPOS(SessionContext ctx, int value)
    {
        setOrderEntryPOS(ctx, Integer.valueOf(value));
    }


    void setOrderEntryPOS(int value)
    {
        setOrderEntryPOS(getSession().getSessionContext(), value);
    }


    public EnumerationValue getProductInfoStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "productInfoStatus");
    }


    public EnumerationValue getProductInfoStatus()
    {
        return getProductInfoStatus(getSession().getSessionContext());
    }


    public void setProductInfoStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "productInfoStatus", value);
    }


    public void setProductInfoStatus(EnumerationValue value)
    {
        setProductInfoStatus(getSession().getSessionContext(), value);
    }
}
