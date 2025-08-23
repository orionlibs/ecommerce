package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRestockConfig extends GenericItem
{
    public static final String ISUPDATESTOCKAFTERRETURN = "isUpdateStockAfterReturn";
    public static final String RETURNEDBINCODE = "returnedBinCode";
    public static final String DELAYDAYSBEFORERESTOCK = "delayDaysBeforeRestock";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("isUpdateStockAfterReturn", Item.AttributeMode.INITIAL);
        tmp.put("returnedBinCode", Item.AttributeMode.INITIAL);
        tmp.put("delayDaysBeforeRestock", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getDelayDaysBeforeRestock(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "delayDaysBeforeRestock");
    }


    public Integer getDelayDaysBeforeRestock()
    {
        return getDelayDaysBeforeRestock(getSession().getSessionContext());
    }


    public int getDelayDaysBeforeRestockAsPrimitive(SessionContext ctx)
    {
        Integer value = getDelayDaysBeforeRestock(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getDelayDaysBeforeRestockAsPrimitive()
    {
        return getDelayDaysBeforeRestockAsPrimitive(getSession().getSessionContext());
    }


    public void setDelayDaysBeforeRestock(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "delayDaysBeforeRestock", value);
    }


    public void setDelayDaysBeforeRestock(Integer value)
    {
        setDelayDaysBeforeRestock(getSession().getSessionContext(), value);
    }


    public void setDelayDaysBeforeRestock(SessionContext ctx, int value)
    {
        setDelayDaysBeforeRestock(ctx, Integer.valueOf(value));
    }


    public void setDelayDaysBeforeRestock(int value)
    {
        setDelayDaysBeforeRestock(getSession().getSessionContext(), value);
    }


    public Boolean isIsUpdateStockAfterReturn(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "isUpdateStockAfterReturn");
    }


    public Boolean isIsUpdateStockAfterReturn()
    {
        return isIsUpdateStockAfterReturn(getSession().getSessionContext());
    }


    public boolean isIsUpdateStockAfterReturnAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIsUpdateStockAfterReturn(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIsUpdateStockAfterReturnAsPrimitive()
    {
        return isIsUpdateStockAfterReturnAsPrimitive(getSession().getSessionContext());
    }


    public void setIsUpdateStockAfterReturn(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "isUpdateStockAfterReturn", value);
    }


    public void setIsUpdateStockAfterReturn(Boolean value)
    {
        setIsUpdateStockAfterReturn(getSession().getSessionContext(), value);
    }


    public void setIsUpdateStockAfterReturn(SessionContext ctx, boolean value)
    {
        setIsUpdateStockAfterReturn(ctx, Boolean.valueOf(value));
    }


    public void setIsUpdateStockAfterReturn(boolean value)
    {
        setIsUpdateStockAfterReturn(getSession().getSessionContext(), value);
    }


    public String getReturnedBinCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "returnedBinCode");
    }


    public String getReturnedBinCode()
    {
        return getReturnedBinCode(getSession().getSessionContext());
    }


    public void setReturnedBinCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "returnedBinCode", value);
    }


    public void setReturnedBinCode(String value)
    {
        setReturnedBinCode(getSession().getSessionContext(), value);
    }
}
