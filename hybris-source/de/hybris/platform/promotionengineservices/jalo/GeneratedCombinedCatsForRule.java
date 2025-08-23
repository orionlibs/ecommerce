package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCombinedCatsForRule extends GenericItem
{
    public static final String RULE = "rule";
    public static final String CONDITIONID = "conditionId";
    public static final String CATEGORYCODE = "categoryCode";
    public static final String PROMOTION = "promotion";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("rule", Item.AttributeMode.INITIAL);
        tmp.put("conditionId", Item.AttributeMode.INITIAL);
        tmp.put("categoryCode", Item.AttributeMode.INITIAL);
        tmp.put("promotion", Item.AttributeMode.INITIAL);
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


    public Integer getConditionId(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "conditionId");
    }


    public Integer getConditionId()
    {
        return getConditionId(getSession().getSessionContext());
    }


    public int getConditionIdAsPrimitive(SessionContext ctx)
    {
        Integer value = getConditionId(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getConditionIdAsPrimitive()
    {
        return getConditionIdAsPrimitive(getSession().getSessionContext());
    }


    protected void setConditionId(SessionContext ctx, Integer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'conditionId' is not changeable", 0);
        }
        setProperty(ctx, "conditionId", value);
    }


    protected void setConditionId(Integer value)
    {
        setConditionId(getSession().getSessionContext(), value);
    }


    protected void setConditionId(SessionContext ctx, int value)
    {
        setConditionId(ctx, Integer.valueOf(value));
    }


    protected void setConditionId(int value)
    {
        setConditionId(getSession().getSessionContext(), value);
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
