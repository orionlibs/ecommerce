package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedProductMultiBuyPromotion extends ProductPromotion
{
    public static final String QUALIFYINGCOUNT = "qualifyingCount";
    public static final String BUNDLEPRICES = "bundlePrices";
    public static final String MESSAGEFIRED = "messageFired";
    public static final String MESSAGECOULDHAVEFIRED = "messageCouldHaveFired";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ProductPromotion.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("qualifyingCount", Item.AttributeMode.INITIAL);
        tmp.put("bundlePrices", Item.AttributeMode.INITIAL);
        tmp.put("messageFired", Item.AttributeMode.INITIAL);
        tmp.put("messageCouldHaveFired", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<PromotionPriceRow> getBundlePrices(SessionContext ctx)
    {
        Collection<PromotionPriceRow> coll = (Collection<PromotionPriceRow>)getProperty(ctx, "bundlePrices");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<PromotionPriceRow> getBundlePrices()
    {
        return getBundlePrices(getSession().getSessionContext());
    }


    public void setBundlePrices(SessionContext ctx, Collection<PromotionPriceRow> value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setBundlePrices(Collection<PromotionPriceRow> value)
    {
        setBundlePrices(getSession().getSessionContext(), value);
    }


    public String getMessageCouldHaveFired(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProductMultiBuyPromotion.getMessageCouldHaveFired requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "messageCouldHaveFired");
    }


    public String getMessageCouldHaveFired()
    {
        return getMessageCouldHaveFired(getSession().getSessionContext());
    }


    public Map<Language, String> getAllMessageCouldHaveFired(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "messageCouldHaveFired", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllMessageCouldHaveFired()
    {
        return getAllMessageCouldHaveFired(getSession().getSessionContext());
    }


    public void setMessageCouldHaveFired(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProductMultiBuyPromotion.setMessageCouldHaveFired requires a session language", 0);
        }
        setLocalizedProperty(ctx, "messageCouldHaveFired", value);
    }


    public void setMessageCouldHaveFired(String value)
    {
        setMessageCouldHaveFired(getSession().getSessionContext(), value);
    }


    public void setAllMessageCouldHaveFired(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "messageCouldHaveFired", value);
    }


    public void setAllMessageCouldHaveFired(Map<Language, String> value)
    {
        setAllMessageCouldHaveFired(getSession().getSessionContext(), value);
    }


    public String getMessageFired(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProductMultiBuyPromotion.getMessageFired requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedProductMultiBuyPromotion.setMessageFired requires a session language", 0);
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


    public Integer getQualifyingCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "qualifyingCount");
    }


    public Integer getQualifyingCount()
    {
        return getQualifyingCount(getSession().getSessionContext());
    }


    public int getQualifyingCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getQualifyingCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getQualifyingCountAsPrimitive()
    {
        return getQualifyingCountAsPrimitive(getSession().getSessionContext());
    }


    public void setQualifyingCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "qualifyingCount", value);
    }


    public void setQualifyingCount(Integer value)
    {
        setQualifyingCount(getSession().getSessionContext(), value);
    }


    public void setQualifyingCount(SessionContext ctx, int value)
    {
        setQualifyingCount(ctx, Integer.valueOf(value));
    }


    public void setQualifyingCount(int value)
    {
        setQualifyingCount(getSession().getSessionContext(), value);
    }
}
