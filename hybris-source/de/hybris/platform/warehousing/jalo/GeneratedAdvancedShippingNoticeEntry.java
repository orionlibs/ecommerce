package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ordersplitting.jalo.StockLevel;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.warehousing.constants.GeneratedWarehousingConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedAdvancedShippingNoticeEntry extends GenericItem
{
    public static final String PRODUCTCODE = "productCode";
    public static final String QUANTITY = "quantity";
    public static final String STOCKLEVELS = "stockLevels";
    public static final String ASN = "asn";
    protected static final OneToManyHandler<StockLevel> STOCKLEVELSHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.STOCKLEVEL, false, "asnEntry", null, false, true, 1);
    protected static final BidirectionalOneToManyHandler<GeneratedAdvancedShippingNoticeEntry> ASNHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.ADVANCEDSHIPPINGNOTICEENTRY, false, "asn", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("productCode", Item.AttributeMode.INITIAL);
        tmp.put("quantity", Item.AttributeMode.INITIAL);
        tmp.put("asn", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public AdvancedShippingNotice getAsn(SessionContext ctx)
    {
        return (AdvancedShippingNotice)getProperty(ctx, "asn");
    }


    public AdvancedShippingNotice getAsn()
    {
        return getAsn(getSession().getSessionContext());
    }


    public void setAsn(SessionContext ctx, AdvancedShippingNotice value)
    {
        ASNHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setAsn(AdvancedShippingNotice value)
    {
        setAsn(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ASNHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getProductCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "productCode");
    }


    public String getProductCode()
    {
        return getProductCode(getSession().getSessionContext());
    }


    public void setProductCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "productCode", value);
    }


    public void setProductCode(String value)
    {
        setProductCode(getSession().getSessionContext(), value);
    }


    public Integer getQuantity(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "quantity");
    }


    public Integer getQuantity()
    {
        return getQuantity(getSession().getSessionContext());
    }


    public int getQuantityAsPrimitive(SessionContext ctx)
    {
        Integer value = getQuantity(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getQuantityAsPrimitive()
    {
        return getQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setQuantity(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "quantity", value);
    }


    public void setQuantity(Integer value)
    {
        setQuantity(getSession().getSessionContext(), value);
    }


    public void setQuantity(SessionContext ctx, int value)
    {
        setQuantity(ctx, Integer.valueOf(value));
    }


    public void setQuantity(int value)
    {
        setQuantity(getSession().getSessionContext(), value);
    }


    public Set<StockLevel> getStockLevels(SessionContext ctx)
    {
        return (Set<StockLevel>)STOCKLEVELSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<StockLevel> getStockLevels()
    {
        return getStockLevels(getSession().getSessionContext());
    }


    public void setStockLevels(SessionContext ctx, Set<StockLevel> value)
    {
        STOCKLEVELSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setStockLevels(Set<StockLevel> value)
    {
        setStockLevels(getSession().getSessionContext(), value);
    }


    public void addToStockLevels(SessionContext ctx, StockLevel value)
    {
        STOCKLEVELSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToStockLevels(StockLevel value)
    {
        addToStockLevels(getSession().getSessionContext(), value);
    }


    public void removeFromStockLevels(SessionContext ctx, StockLevel value)
    {
        STOCKLEVELSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromStockLevels(StockLevel value)
    {
        removeFromStockLevels(getSession().getSessionContext(), value);
    }
}
