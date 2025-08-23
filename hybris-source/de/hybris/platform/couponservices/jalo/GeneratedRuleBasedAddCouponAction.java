package de.hybris.platform.couponservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.promotionengineservices.jalo.AbstractRuleBasedPromotionAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRuleBasedAddCouponAction extends AbstractRuleBasedPromotionAction
{
    public static final String COUPONID = "couponId";
    public static final String COUPONCODE = "couponCode";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRuleBasedPromotionAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("couponId", Item.AttributeMode.INITIAL);
        tmp.put("couponCode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
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


    public String getCouponId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "couponId");
    }


    public String getCouponId()
    {
        return getCouponId(getSession().getSessionContext());
    }


    public void setCouponId(SessionContext ctx, String value)
    {
        setProperty(ctx, "couponId", value);
    }


    public void setCouponId(String value)
    {
        setCouponId(getSession().getSessionContext(), value);
    }
}
