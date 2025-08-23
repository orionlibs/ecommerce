package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.warehousing.constants.GeneratedWarehousingConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedWarehouseBinEntry extends GenericItem
{
    public static final String PRODUCTCODE = "productCode";
    public static final String QUANTITY = "quantity";
    public static final String WAREHOUSEBIN = "warehouseBin";
    protected static final BidirectionalOneToManyHandler<GeneratedWarehouseBinEntry> WAREHOUSEBINHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.WAREHOUSEBINENTRY, false, "warehouseBin", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("productCode", Item.AttributeMode.INITIAL);
        tmp.put("quantity", Item.AttributeMode.INITIAL);
        tmp.put("warehouseBin", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        WAREHOUSEBINHANDLER.newInstance(ctx, allAttributes);
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


    public WarehouseBin getWarehouseBin(SessionContext ctx)
    {
        return (WarehouseBin)getProperty(ctx, "warehouseBin");
    }


    public WarehouseBin getWarehouseBin()
    {
        return getWarehouseBin(getSession().getSessionContext());
    }


    public void setWarehouseBin(SessionContext ctx, WarehouseBin value)
    {
        WAREHOUSEBINHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setWarehouseBin(WarehouseBin value)
    {
        setWarehouseBin(getSession().getSessionContext(), value);
    }
}
