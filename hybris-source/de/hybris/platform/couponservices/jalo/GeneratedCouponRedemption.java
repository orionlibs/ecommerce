package de.hybris.platform.couponservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.user.User;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCouponRedemption extends GenericItem
{
    public static final String COUPONCODE = "couponCode";
    public static final String COUPON = "coupon";
    public static final String ORDER = "order";
    public static final String USER = "user";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("couponCode", Item.AttributeMode.INITIAL);
        tmp.put("coupon", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public AbstractCoupon getCoupon(SessionContext ctx)
    {
        return (AbstractCoupon)getProperty(ctx, "coupon");
    }


    public AbstractCoupon getCoupon()
    {
        return getCoupon(getSession().getSessionContext());
    }


    public void setCoupon(SessionContext ctx, AbstractCoupon value)
    {
        setProperty(ctx, "coupon", value);
    }


    public void setCoupon(AbstractCoupon value)
    {
        setCoupon(getSession().getSessionContext(), value);
    }


    public String getCouponCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "couponCode");
    }


    public String getCouponCode()
    {
        return getCouponCode(getSession().getSessionContext());
    }


    public void setCouponCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "couponCode", value);
    }


    public void setCouponCode(String value)
    {
        setCouponCode(getSession().getSessionContext(), value);
    }


    public AbstractOrder getOrder(SessionContext ctx)
    {
        return (AbstractOrder)getProperty(ctx, "order");
    }


    public AbstractOrder getOrder()
    {
        return getOrder(getSession().getSessionContext());
    }


    public void setOrder(SessionContext ctx, AbstractOrder value)
    {
        setProperty(ctx, "order", value);
    }


    public void setOrder(AbstractOrder value)
    {
        setOrder(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        setProperty(ctx, "user", value);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }
}
