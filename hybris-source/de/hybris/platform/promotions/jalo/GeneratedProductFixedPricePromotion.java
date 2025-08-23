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

public abstract class GeneratedProductFixedPricePromotion extends ProductPromotion
{
    public static final String PRODUCTFIXEDUNITPRICE = "productFixedUnitPrice";
    public static final String MESSAGEFIRED = "messageFired";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ProductPromotion.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("productFixedUnitPrice", Item.AttributeMode.INITIAL);
        tmp.put("messageFired", Item.AttributeMode.INITIAL);
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
            throw new JaloInvalidParameterException("GeneratedProductFixedPricePromotion.getMessageFired requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedProductFixedPricePromotion.setMessageFired requires a session language", 0);
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


    public Collection<PromotionPriceRow> getProductFixedUnitPrice(SessionContext ctx)
    {
        Collection<PromotionPriceRow> coll = (Collection<PromotionPriceRow>)getProperty(ctx, "productFixedUnitPrice");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<PromotionPriceRow> getProductFixedUnitPrice()
    {
        return getProductFixedUnitPrice(getSession().getSessionContext());
    }


    public void setProductFixedUnitPrice(SessionContext ctx, Collection<PromotionPriceRow> value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setProductFixedUnitPrice(Collection<PromotionPriceRow> value)
    {
        setProductFixedUnitPrice(getSession().getSessionContext(), value);
    }
}
