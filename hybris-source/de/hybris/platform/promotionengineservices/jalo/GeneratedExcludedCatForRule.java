package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedExcludedCatForRule extends GenericItem
{
    public static final String CATEGORYCODE = "categoryCode";
    public static final String RULE = "rule";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("categoryCode", Item.AttributeMode.INITIAL);
        tmp.put("rule", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCategoryCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "categoryCode");
    }


    public String getCategoryCode()
    {
        return getCategoryCode(getSession().getSessionContext());
    }


    protected void setCategoryCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'categoryCode' is not changeable", 0);
        }
        setProperty(ctx, "categoryCode", value);
    }


    protected void setCategoryCode(String value)
    {
        setCategoryCode(getSession().getSessionContext(), value);
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
