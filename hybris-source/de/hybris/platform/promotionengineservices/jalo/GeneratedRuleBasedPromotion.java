package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.ruleengine.jalo.AbstractRuleEngineRule;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRuleBasedPromotion extends AbstractPromotion
{
    public static final String MESSAGEFIRED = "messageFired";
    public static final String RULE = "rule";
    public static final String PROMOTIONDESCRIPTION = "promotionDescription";
    public static final String RULEVERSION = "ruleVersion";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractPromotion.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("messageFired", Item.AttributeMode.INITIAL);
        tmp.put("rule", Item.AttributeMode.INITIAL);
        tmp.put("promotionDescription", Item.AttributeMode.INITIAL);
        tmp.put("ruleVersion", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getMessageFired(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleBasedPromotion.getMessageFired requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "messageFired");
    }


    public String getMessageFired()
    {
        return getMessageFired(getSession().getSessionContext());
    }


    public Map<Language, String> getAllMessageFired(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "messageFired", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllMessageFired()
    {
        return getAllMessageFired(getSession().getSessionContext());
    }


    public void setMessageFired(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleBasedPromotion.setMessageFired requires a session language", 0);
        }
        setLocalizedProperty(ctx, "messageFired", value);
    }


    public void setMessageFired(String value)
    {
        setMessageFired(getSession().getSessionContext(), value);
    }


    public void setAllMessageFired(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "messageFired", value);
    }


    public void setAllMessageFired(Map<Language, String> value)
    {
        setAllMessageFired(getSession().getSessionContext(), value);
    }


    public String getPromotionDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleBasedPromotion.getPromotionDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "promotionDescription");
    }


    public String getPromotionDescription()
    {
        return getPromotionDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllPromotionDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "promotionDescription", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllPromotionDescription()
    {
        return getAllPromotionDescription(getSession().getSessionContext());
    }


    public void setPromotionDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedRuleBasedPromotion.setPromotionDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "promotionDescription", value);
    }


    public void setPromotionDescription(String value)
    {
        setPromotionDescription(getSession().getSessionContext(), value);
    }


    public void setAllPromotionDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "promotionDescription", value);
    }


    public void setAllPromotionDescription(Map<Language, String> value)
    {
        setAllPromotionDescription(getSession().getSessionContext(), value);
    }


    public AbstractRuleEngineRule getRule(SessionContext ctx)
    {
        return (AbstractRuleEngineRule)getProperty(ctx, "rule");
    }


    public AbstractRuleEngineRule getRule()
    {
        return getRule(getSession().getSessionContext());
    }


    public void setRule(SessionContext ctx, AbstractRuleEngineRule value)
    {
        setProperty(ctx, "rule", value);
    }


    public void setRule(AbstractRuleEngineRule value)
    {
        setRule(getSession().getSessionContext(), value);
    }


    public Long getRuleVersion(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "ruleVersion");
    }


    public Long getRuleVersion()
    {
        return getRuleVersion(getSession().getSessionContext());
    }


    public long getRuleVersionAsPrimitive(SessionContext ctx)
    {
        Long value = getRuleVersion(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getRuleVersionAsPrimitive()
    {
        return getRuleVersionAsPrimitive(getSession().getSessionContext());
    }


    public void setRuleVersion(SessionContext ctx, Long value)
    {
        setProperty(ctx, "ruleVersion", value);
    }


    public void setRuleVersion(Long value)
    {
        setRuleVersion(getSession().getSessionContext(), value);
    }


    public void setRuleVersion(SessionContext ctx, long value)
    {
        setRuleVersion(ctx, Long.valueOf(value));
    }


    public void setRuleVersion(long value)
    {
        setRuleVersion(getSession().getSessionContext(), value);
    }
}
