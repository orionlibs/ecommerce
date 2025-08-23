package de.hybris.platform.stock.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ordersplitting.jalo.StockLevel;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedStockLevelHistoryEntry extends GenericItem
{
    public static final String UPDATEDATE = "updateDate";
    public static final String ACTUAL = "actual";
    public static final String RESERVED = "reserved";
    public static final String UPDATETYPE = "updateType";
    public static final String COMMENT = "comment";
    public static final String STOCKLEVELPOS = "stockLevelPOS";
    public static final String STOCKLEVEL = "stockLevel";
    protected static final BidirectionalOneToManyHandler<GeneratedStockLevelHistoryEntry> STOCKLEVELHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.STOCKLEVELHISTORYENTRY, false, "stockLevel", "stockLevelPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("updateDate", Item.AttributeMode.INITIAL);
        tmp.put("actual", Item.AttributeMode.INITIAL);
        tmp.put("reserved", Item.AttributeMode.INITIAL);
        tmp.put("updateType", Item.AttributeMode.INITIAL);
        tmp.put("comment", Item.AttributeMode.INITIAL);
        tmp.put("stockLevelPOS", Item.AttributeMode.INITIAL);
        tmp.put("stockLevel", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getActual(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "actual");
    }


    public Integer getActual()
    {
        return getActual(getSession().getSessionContext());
    }


    public int getActualAsPrimitive(SessionContext ctx)
    {
        Integer value = getActual(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getActualAsPrimitive()
    {
        return getActualAsPrimitive(getSession().getSessionContext());
    }


    public void setActual(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "actual", value);
    }


    public void setActual(Integer value)
    {
        setActual(getSession().getSessionContext(), value);
    }


    public void setActual(SessionContext ctx, int value)
    {
        setActual(ctx, Integer.valueOf(value));
    }


    public void setActual(int value)
    {
        setActual(getSession().getSessionContext(), value);
    }


    public String getComment(SessionContext ctx)
    {
        return (String)getProperty(ctx, "comment");
    }


    public String getComment()
    {
        return getComment(getSession().getSessionContext());
    }


    public void setComment(SessionContext ctx, String value)
    {
        setProperty(ctx, "comment", value);
    }


    public void setComment(String value)
    {
        setComment(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        STOCKLEVELHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Integer getReserved(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "reserved");
    }


    public Integer getReserved()
    {
        return getReserved(getSession().getSessionContext());
    }


    public int getReservedAsPrimitive(SessionContext ctx)
    {
        Integer value = getReserved(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getReservedAsPrimitive()
    {
        return getReservedAsPrimitive(getSession().getSessionContext());
    }


    public void setReserved(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "reserved", value);
    }


    public void setReserved(Integer value)
    {
        setReserved(getSession().getSessionContext(), value);
    }


    public void setReserved(SessionContext ctx, int value)
    {
        setReserved(ctx, Integer.valueOf(value));
    }


    public void setReserved(int value)
    {
        setReserved(getSession().getSessionContext(), value);
    }


    public StockLevel getStockLevel(SessionContext ctx)
    {
        return (StockLevel)getProperty(ctx, "stockLevel");
    }


    public StockLevel getStockLevel()
    {
        return getStockLevel(getSession().getSessionContext());
    }


    protected void setStockLevel(SessionContext ctx, StockLevel value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'stockLevel' is not changeable", 0);
        }
        STOCKLEVELHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setStockLevel(StockLevel value)
    {
        setStockLevel(getSession().getSessionContext(), value);
    }


    Integer getStockLevelPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "stockLevelPOS");
    }


    Integer getStockLevelPOS()
    {
        return getStockLevelPOS(getSession().getSessionContext());
    }


    int getStockLevelPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getStockLevelPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getStockLevelPOSAsPrimitive()
    {
        return getStockLevelPOSAsPrimitive(getSession().getSessionContext());
    }


    void setStockLevelPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "stockLevelPOS", value);
    }


    void setStockLevelPOS(Integer value)
    {
        setStockLevelPOS(getSession().getSessionContext(), value);
    }


    void setStockLevelPOS(SessionContext ctx, int value)
    {
        setStockLevelPOS(ctx, Integer.valueOf(value));
    }


    void setStockLevelPOS(int value)
    {
        setStockLevelPOS(getSession().getSessionContext(), value);
    }


    public Date getUpdateDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "updateDate");
    }


    public Date getUpdateDate()
    {
        return getUpdateDate(getSession().getSessionContext());
    }


    public void setUpdateDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "updateDate", value);
    }


    public void setUpdateDate(Date value)
    {
        setUpdateDate(getSession().getSessionContext(), value);
    }


    public EnumerationValue getUpdateType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "updateType");
    }


    public EnumerationValue getUpdateType()
    {
        return getUpdateType(getSession().getSessionContext());
    }


    public void setUpdateType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "updateType", value);
    }


    public void setUpdateType(EnumerationValue value)
    {
        setUpdateType(getSession().getSessionContext(), value);
    }
}
