package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRuleBasedOrderEntryAdjustAction extends AbstractRuleBasedPromotionAction
{
    public static final String AMOUNT = "amount";
    public static final String ORDERENTRYPRODUCT = "orderEntryProduct";
    public static final String ORDERENTRYQUANTITY = "orderEntryQuantity";
    public static final String ORDERENTRYNUMBER = "orderEntryNumber";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRuleBasedPromotionAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("amount", Item.AttributeMode.INITIAL);
        tmp.put("orderEntryProduct", Item.AttributeMode.INITIAL);
        tmp.put("orderEntryQuantity", Item.AttributeMode.INITIAL);
        tmp.put("orderEntryNumber", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public BigDecimal getAmount(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "amount");
    }


    public BigDecimal getAmount()
    {
        return getAmount(getSession().getSessionContext());
    }


    public void setAmount(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "amount", value);
    }


    public void setAmount(BigDecimal value)
    {
        setAmount(getSession().getSessionContext(), value);
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


    public Product getOrderEntryProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "orderEntryProduct");
    }


    public Product getOrderEntryProduct()
    {
        return getOrderEntryProduct(getSession().getSessionContext());
    }


    public void setOrderEntryProduct(SessionContext ctx, Product value)
    {
        setProperty(ctx, "orderEntryProduct", value);
    }


    public void setOrderEntryProduct(Product value)
    {
        setOrderEntryProduct(getSession().getSessionContext(), value);
    }


    public Long getOrderEntryQuantity(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "orderEntryQuantity");
    }


    public Long getOrderEntryQuantity()
    {
        return getOrderEntryQuantity(getSession().getSessionContext());
    }


    public long getOrderEntryQuantityAsPrimitive(SessionContext ctx)
    {
        Long value = getOrderEntryQuantity(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getOrderEntryQuantityAsPrimitive()
    {
        return getOrderEntryQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setOrderEntryQuantity(SessionContext ctx, Long value)
    {
        setProperty(ctx, "orderEntryQuantity", value);
    }


    public void setOrderEntryQuantity(Long value)
    {
        setOrderEntryQuantity(getSession().getSessionContext(), value);
    }


    public void setOrderEntryQuantity(SessionContext ctx, long value)
    {
        setOrderEntryQuantity(ctx, Long.valueOf(value));
    }


    public void setOrderEntryQuantity(long value)
    {
        setOrderEntryQuantity(getSession().getSessionContext(), value);
    }
}
