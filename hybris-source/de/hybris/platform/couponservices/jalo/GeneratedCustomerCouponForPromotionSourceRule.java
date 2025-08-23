package de.hybris.platform.couponservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.promotionengineservices.jalo.PromotionSourceRule;
import de.hybris.platform.promotionengineservices.jalo.RuleBasedPromotion;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCustomerCouponForPromotionSourceRule extends GenericItem
{
    public static final String CUSTOMERCOUPONCODE = "customerCouponCode";
    public static final String RULE = "rule";
    public static final String PROMOTION = "promotion";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("customerCouponCode", Item.AttributeMode.INITIAL);
        tmp.put("rule", Item.AttributeMode.INITIAL);
        tmp.put("promotion", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCustomerCouponCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "customerCouponCode");
    }


    public String getCustomerCouponCode()
    {
        return getCustomerCouponCode(getSession().getSessionContext());
    }


    protected void setCustomerCouponCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'customerCouponCode' is not changeable", 0);
        }
        setProperty(ctx, "customerCouponCode", value);
    }


    protected void setCustomerCouponCode(String value)
    {
        setCustomerCouponCode(getSession().getSessionContext(), value);
    }


    public RuleBasedPromotion getPromotion(SessionContext ctx)
    {
        return (RuleBasedPromotion)getProperty(ctx, "promotion");
    }


    public RuleBasedPromotion getPromotion()
    {
        return getPromotion(getSession().getSessionContext());
    }


    protected void setPromotion(SessionContext ctx, RuleBasedPromotion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'promotion' is not changeable", 0);
        }
        setProperty(ctx, "promotion", value);
    }


    protected void setPromotion(RuleBasedPromotion value)
    {
        setPromotion(getSession().getSessionContext(), value);
    }


    public PromotionSourceRule getRule(SessionContext ctx)
    {
        return (PromotionSourceRule)getProperty(ctx, "rule");
    }


    public PromotionSourceRule getRule()
    {
        return getRule(getSession().getSessionContext());
    }


    protected void setRule(SessionContext ctx, PromotionSourceRule value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'rule' is not changeable", 0);
        }
        setProperty(ctx, "rule", value);
    }


    protected void setRule(PromotionSourceRule value)
    {
        setRule(getSession().getSessionContext(), value);
    }
}
