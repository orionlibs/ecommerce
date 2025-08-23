package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionVoucher extends Voucher
{
    public static final String VOUCHERCODE = "voucherCode";
    public static final String REDEMPTIONQUANTITYLIMIT = "redemptionQuantityLimit";
    public static final String REDEMPTIONQUANTITYLIMITPERUSER = "redemptionQuantityLimitPerUser";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Voucher.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("voucherCode", Item.AttributeMode.INITIAL);
        tmp.put("redemptionQuantityLimit", Item.AttributeMode.INITIAL);
        tmp.put("redemptionQuantityLimitPerUser", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getRedemptionQuantityLimit(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "redemptionQuantityLimit");
    }


    public Integer getRedemptionQuantityLimit()
    {
        return getRedemptionQuantityLimit(getSession().getSessionContext());
    }


    public int getRedemptionQuantityLimitAsPrimitive(SessionContext ctx)
    {
        Integer value = getRedemptionQuantityLimit(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getRedemptionQuantityLimitAsPrimitive()
    {
        return getRedemptionQuantityLimitAsPrimitive(getSession().getSessionContext());
    }


    public void setRedemptionQuantityLimit(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "redemptionQuantityLimit", value);
    }


    public void setRedemptionQuantityLimit(Integer value)
    {
        setRedemptionQuantityLimit(getSession().getSessionContext(), value);
    }


    public void setRedemptionQuantityLimit(SessionContext ctx, int value)
    {
        setRedemptionQuantityLimit(ctx, Integer.valueOf(value));
    }


    public void setRedemptionQuantityLimit(int value)
    {
        setRedemptionQuantityLimit(getSession().getSessionContext(), value);
    }


    public Integer getRedemptionQuantityLimitPerUser(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "redemptionQuantityLimitPerUser");
    }


    public Integer getRedemptionQuantityLimitPerUser()
    {
        return getRedemptionQuantityLimitPerUser(getSession().getSessionContext());
    }


    public int getRedemptionQuantityLimitPerUserAsPrimitive(SessionContext ctx)
    {
        Integer value = getRedemptionQuantityLimitPerUser(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getRedemptionQuantityLimitPerUserAsPrimitive()
    {
        return getRedemptionQuantityLimitPerUserAsPrimitive(getSession().getSessionContext());
    }


    public void setRedemptionQuantityLimitPerUser(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "redemptionQuantityLimitPerUser", value);
    }


    public void setRedemptionQuantityLimitPerUser(Integer value)
    {
        setRedemptionQuantityLimitPerUser(getSession().getSessionContext(), value);
    }


    public void setRedemptionQuantityLimitPerUser(SessionContext ctx, int value)
    {
        setRedemptionQuantityLimitPerUser(ctx, Integer.valueOf(value));
    }


    public void setRedemptionQuantityLimitPerUser(int value)
    {
        setRedemptionQuantityLimitPerUser(getSession().getSessionContext(), value);
    }


    public String getVoucherCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "voucherCode");
    }


    public String getVoucherCode()
    {
        return getVoucherCode(getSession().getSessionContext());
    }


    public void setVoucherCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "voucherCode", value);
    }


    public void setVoucherCode(String value)
    {
        setVoucherCode(getSession().getSessionContext(), value);
    }
}
