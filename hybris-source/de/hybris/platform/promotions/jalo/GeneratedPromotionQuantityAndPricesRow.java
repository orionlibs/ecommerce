package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionQuantityAndPricesRow extends GenericItem
{
    public static final String QUANTITY = "quantity";
    public static final String PRICES = "prices";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("quantity", Item.AttributeMode.INITIAL);
        tmp.put("prices", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<PromotionPriceRow> getPrices(SessionContext ctx)
    {
        Collection<PromotionPriceRow> coll = (Collection<PromotionPriceRow>)getProperty(ctx, "prices");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<PromotionPriceRow> getPrices()
    {
        return getPrices(getSession().getSessionContext());
    }


    public void setPrices(SessionContext ctx, Collection<PromotionPriceRow> value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setPrices(Collection<PromotionPriceRow> value)
    {
        setPrices(getSession().getSessionContext(), value);
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
