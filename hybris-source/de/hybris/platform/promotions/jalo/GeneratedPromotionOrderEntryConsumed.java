package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionOrderEntryConsumed extends GenericItem
{
    public static final String CODE = "code";
    public static final String PROMOTIONRESULT = "promotionResult";
    public static final String QUANTITY = "quantity";
    public static final String ADJUSTEDUNITPRICE = "adjustedUnitPrice";
    public static final String ORDERENTRY = "orderEntry";
    public static final String ORDERENTRYNUMBER = "orderEntryNumber";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("promotionResult", Item.AttributeMode.INITIAL);
        tmp.put("quantity", Item.AttributeMode.INITIAL);
        tmp.put("adjustedUnitPrice", Item.AttributeMode.INITIAL);
        tmp.put("orderEntry", Item.AttributeMode.INITIAL);
        tmp.put("orderEntryNumber", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Double getAdjustedUnitPrice(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "adjustedUnitPrice");
    }


    public Double getAdjustedUnitPrice()
    {
        return getAdjustedUnitPrice(getSession().getSessionContext());
    }


    public double getAdjustedUnitPriceAsPrimitive(SessionContext ctx)
    {
        Double value = getAdjustedUnitPrice(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getAdjustedUnitPriceAsPrimitive()
    {
        return getAdjustedUnitPriceAsPrimitive(getSession().getSessionContext());
    }


    public void setAdjustedUnitPrice(SessionContext ctx, Double value)
    {
        setProperty(ctx, "adjustedUnitPrice", value);
    }


    public void setAdjustedUnitPrice(Double value)
    {
        setAdjustedUnitPrice(getSession().getSessionContext(), value);
    }


    public void setAdjustedUnitPrice(SessionContext ctx, double value)
    {
        setAdjustedUnitPrice(ctx, Double.valueOf(value));
    }


    public void setAdjustedUnitPrice(double value)
    {
        setAdjustedUnitPrice(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public AbstractOrderEntry getOrderEntry(SessionContext ctx)
    {
        return (AbstractOrderEntry)getProperty(ctx, "orderEntry");
    }


    public AbstractOrderEntry getOrderEntry()
    {
        return getOrderEntry(getSession().getSessionContext());
    }


    public void setOrderEntry(SessionContext ctx, AbstractOrderEntry value)
    {
        setProperty(ctx, "orderEntry", value);
    }


    public void setOrderEntry(AbstractOrderEntry value)
    {
        setOrderEntry(getSession().getSessionContext(), value);
    }


    public Integer getOrderEntryNumber(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "orderEntryNumber");
    }


    public Integer getOrderEntryNumber()
    {
        return getOrderEntryNumber(getSession().getSessionContext());
    }


    public int getOrderEntryNumberAsPrimitive(SessionContext ctx)
    {
        Integer value = getOrderEntryNumber(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getOrderEntryNumberAsPrimitive()
    {
        return getOrderEntryNumberAsPrimitive(getSession().getSessionContext());
    }


    public void setOrderEntryNumber(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "orderEntryNumber", value);
    }


    public void setOrderEntryNumber(Integer value)
    {
        setOrderEntryNumber(getSession().getSessionContext(), value);
    }


    public void setOrderEntryNumber(SessionContext ctx, int value)
    {
        setOrderEntryNumber(ctx, Integer.valueOf(value));
    }


    public void setOrderEntryNumber(int value)
    {
        setOrderEntryNumber(getSession().getSessionContext(), value);
    }


    public PromotionResult getPromotionResult(SessionContext ctx)
    {
        return (PromotionResult)getProperty(ctx, "promotionResult");
    }


    public PromotionResult getPromotionResult()
    {
        return getPromotionResult(getSession().getSessionContext());
    }


    public void setPromotionResult(SessionContext ctx, PromotionResult value)
    {
        setProperty(ctx, "promotionResult", value);
    }


    public void setPromotionResult(PromotionResult value)
    {
        setPromotionResult(getSession().getSessionContext(), value);
    }


    public Long getQuantity(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "quantity");
    }


    public Long getQuantity()
    {
        return getQuantity(getSession().getSessionContext());
    }


    public long getQuantityAsPrimitive(SessionContext ctx)
    {
        Long value = getQuantity(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getQuantityAsPrimitive()
    {
        return getQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setQuantity(SessionContext ctx, Long value)
    {
        setProperty(ctx, "quantity", value);
    }


    public void setQuantity(Long value)
    {
        setQuantity(getSession().getSessionContext(), value);
    }


    public void setQuantity(SessionContext ctx, long value)
    {
        setQuantity(ctx, Long.valueOf(value));
    }


    public void setQuantity(long value)
    {
        setQuantity(getSession().getSessionContext(), value);
    }
}
