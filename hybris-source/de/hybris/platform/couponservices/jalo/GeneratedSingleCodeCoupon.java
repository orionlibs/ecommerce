package de.hybris.platform.couponservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSingleCodeCoupon extends AbstractCoupon
{
    public static final String MAXREDEMPTIONSPERCUSTOMER = "maxRedemptionsPerCustomer";
    public static final String MAXTOTALREDEMPTIONS = "maxTotalRedemptions";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractCoupon.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("maxRedemptionsPerCustomer", Item.AttributeMode.INITIAL);
        tmp.put("maxTotalRedemptions", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getMaxRedemptionsPerCustomer(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxRedemptionsPerCustomer");
    }


    public Integer getMaxRedemptionsPerCustomer()
    {
        return getMaxRedemptionsPerCustomer(getSession().getSessionContext());
    }


    public int getMaxRedemptionsPerCustomerAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxRedemptionsPerCustomer(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxRedemptionsPerCustomerAsPrimitive()
    {
        return getMaxRedemptionsPerCustomerAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxRedemptionsPerCustomer(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxRedemptionsPerCustomer", value);
    }


    public void setMaxRedemptionsPerCustomer(Integer value)
    {
        setMaxRedemptionsPerCustomer(getSession().getSessionContext(), value);
    }


    public void setMaxRedemptionsPerCustomer(SessionContext ctx, int value)
    {
        setMaxRedemptionsPerCustomer(ctx, Integer.valueOf(value));
    }


    public void setMaxRedemptionsPerCustomer(int value)
    {
        setMaxRedemptionsPerCustomer(getSession().getSessionContext(), value);
    }


    public Integer getMaxTotalRedemptions(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxTotalRedemptions");
    }


    public Integer getMaxTotalRedemptions()
    {
        return getMaxTotalRedemptions(getSession().getSessionContext());
    }


    public int getMaxTotalRedemptionsAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxTotalRedemptions(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxTotalRedemptionsAsPrimitive()
    {
        return getMaxTotalRedemptionsAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxTotalRedemptions(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxTotalRedemptions", value);
    }


    public void setMaxTotalRedemptions(Integer value)
    {
        setMaxTotalRedemptions(getSession().getSessionContext(), value);
    }


    public void setMaxTotalRedemptions(SessionContext ctx, int value)
    {
        setMaxTotalRedemptions(ctx, Integer.valueOf(value));
    }


    public void setMaxTotalRedemptions(int value)
    {
        setMaxTotalRedemptions(getSession().getSessionContext(), value);
    }
}
