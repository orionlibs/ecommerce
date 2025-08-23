package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedOrderThresholdPerfectPartnerPromotion extends OrderPromotion
{
    public static final String THRESHOLDTOTALS = "thresholdTotals";
    public static final String DISCOUNTPRODUCT = "discountProduct";
    public static final String PRODUCTPRICES = "productPrices";
    public static final String INCLUDEDISCOUNTEDPRICEINTHRESHOLD = "includeDiscountedPriceInThreshold";
    public static final String MESSAGEFIRED = "messageFired";
    public static final String MESSAGECOULDHAVEFIRED = "messageCouldHaveFired";
    public static final String MESSAGEPRODUCTNOTHRESHOLD = "messageProductNoThreshold";
    public static final String MESSAGETHRESHOLDNOPRODUCT = "messageThresholdNoProduct";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(OrderPromotion.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("thresholdTotals", Item.AttributeMode.INITIAL);
        tmp.put("discountProduct", Item.AttributeMode.INITIAL);
        tmp.put("productPrices", Item.AttributeMode.INITIAL);
        tmp.put("includeDiscountedPriceInThreshold", Item.AttributeMode.INITIAL);
        tmp.put("messageFired", Item.AttributeMode.INITIAL);
        tmp.put("messageCouldHaveFired", Item.AttributeMode.INITIAL);
        tmp.put("messageProductNoThreshold", Item.AttributeMode.INITIAL);
        tmp.put("messageThresholdNoProduct", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Product getDiscountProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "discountProduct");
    }


    public Product getDiscountProduct()
    {
        return getDiscountProduct(getSession().getSessionContext());
    }


    public void setDiscountProduct(SessionContext ctx, Product value)
    {
        setProperty(ctx, "discountProduct", value);
    }


    public void setDiscountProduct(Product value)
    {
        setDiscountProduct(getSession().getSessionContext(), value);
    }


    public Boolean isIncludeDiscountedPriceInThreshold(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "includeDiscountedPriceInThreshold");
    }


    public Boolean isIncludeDiscountedPriceInThreshold()
    {
        return isIncludeDiscountedPriceInThreshold(getSession().getSessionContext());
    }


    public boolean isIncludeDiscountedPriceInThresholdAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIncludeDiscountedPriceInThreshold(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIncludeDiscountedPriceInThresholdAsPrimitive()
    {
        return isIncludeDiscountedPriceInThresholdAsPrimitive(getSession().getSessionContext());
    }


    public void setIncludeDiscountedPriceInThreshold(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "includeDiscountedPriceInThreshold", value);
    }


    public void setIncludeDiscountedPriceInThreshold(Boolean value)
    {
        setIncludeDiscountedPriceInThreshold(getSession().getSessionContext(), value);
    }


    public void setIncludeDiscountedPriceInThreshold(SessionContext ctx, boolean value)
    {
        setIncludeDiscountedPriceInThreshold(ctx, Boolean.valueOf(value));
    }


    public void setIncludeDiscountedPriceInThreshold(boolean value)
    {
        setIncludeDiscountedPriceInThreshold(getSession().getSessionContext(), value);
    }


    public String getMessageCouldHaveFired(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedOrderThresholdPerfectPartnerPromotion.getMessageCouldHaveFired requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedOrderThresholdPerfectPartnerPromotion.setMessageCouldHaveFired requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedOrderThresholdPerfectPartnerPromotion.getMessageFired requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedOrderThresholdPerfectPartnerPromotion.setMessageFired requires a session language", 0);
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


    public String getMessageProductNoThreshold(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedOrderThresholdPerfectPartnerPromotion.getMessageProductNoThreshold requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "messageProductNoThreshold");
    }


    public String getMessageProductNoThreshold()
    {
        return getMessageProductNoThreshold(getSession().getSessionContext());
    }


    public Map<Language, String> getAllMessageProductNoThreshold(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "messageProductNoThreshold", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllMessageProductNoThreshold()
    {
        return getAllMessageProductNoThreshold(getSession().getSessionContext());
    }


    public void setMessageProductNoThreshold(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedOrderThresholdPerfectPartnerPromotion.setMessageProductNoThreshold requires a session language", 0);
        }
        setLocalizedProperty(ctx, "messageProductNoThreshold", value);
    }


    public void setMessageProductNoThreshold(String value)
    {
        setMessageProductNoThreshold(getSession().getSessionContext(), value);
    }


    public void setAllMessageProductNoThreshold(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "messageProductNoThreshold", value);
    }


    public void setAllMessageProductNoThreshold(Map<Language, String> value)
    {
        setAllMessageProductNoThreshold(getSession().getSessionContext(), value);
    }


    public String getMessageThresholdNoProduct(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedOrderThresholdPerfectPartnerPromotion.getMessageThresholdNoProduct requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "messageThresholdNoProduct");
    }


    public String getMessageThresholdNoProduct()
    {
        return getMessageThresholdNoProduct(getSession().getSessionContext());
    }


    public Map<Language, String> getAllMessageThresholdNoProduct(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "messageThresholdNoProduct", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllMessageThresholdNoProduct()
    {
        return getAllMessageThresholdNoProduct(getSession().getSessionContext());
    }


    public void setMessageThresholdNoProduct(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedOrderThresholdPerfectPartnerPromotion.setMessageThresholdNoProduct requires a session language", 0);
        }
        setLocalizedProperty(ctx, "messageThresholdNoProduct", value);
    }


    public void setMessageThresholdNoProduct(String value)
    {
        setMessageThresholdNoProduct(getSession().getSessionContext(), value);
    }


    public void setAllMessageThresholdNoProduct(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "messageThresholdNoProduct", value);
    }


    public void setAllMessageThresholdNoProduct(Map<Language, String> value)
    {
        setAllMessageThresholdNoProduct(getSession().getSessionContext(), value);
    }


    public Collection<PromotionPriceRow> getProductPrices(SessionContext ctx)
    {
        Collection<PromotionPriceRow> coll = (Collection<PromotionPriceRow>)getProperty(ctx, "productPrices");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<PromotionPriceRow> getProductPrices()
    {
        return getProductPrices(getSession().getSessionContext());
    }


    public void setProductPrices(SessionContext ctx, Collection<PromotionPriceRow> value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setProductPrices(Collection<PromotionPriceRow> value)
    {
        setProductPrices(getSession().getSessionContext(), value);
    }


    public Collection<PromotionPriceRow> getThresholdTotals(SessionContext ctx)
    {
        Collection<PromotionPriceRow> coll = (Collection<PromotionPriceRow>)getProperty(ctx, "thresholdTotals");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<PromotionPriceRow> getThresholdTotals()
    {
        return getThresholdTotals(getSession().getSessionContext());
    }


    public void setThresholdTotals(SessionContext ctx, Collection<PromotionPriceRow> value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setThresholdTotals(Collection<PromotionPriceRow> value)
    {
        setThresholdTotals(getSession().getSessionContext(), value);
    }
}
