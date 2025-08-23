package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionOrderAdjustTotalAction extends AbstractPromotionAction
{
    public static final String AMOUNT = "amount";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractPromotionAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("amount", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Double getAmount(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "amount");
    }


    public Double getAmount()
    {
        return getAmount(getSession().getSessionContext());
    }


    public double getAmountAsPrimitive(SessionContext ctx)
    {
        Double value = getAmount(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getAmountAsPrimitive()
    {
        return getAmountAsPrimitive(getSession().getSessionContext());
    }


    public void setAmount(SessionContext ctx, Double value)
    {
        setProperty(ctx, "amount", value);
    }


    public void setAmount(Double value)
    {
        setAmount(getSession().getSessionContext(), value);
    }


    public void setAmount(SessionContext ctx, double value)
    {
        setAmount(ctx, Double.valueOf(value));
    }


    public void setAmount(double value)
    {
        setAmount(getSession().getSessionContext(), value);
    }
}
