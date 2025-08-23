package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractPromotionRestriction extends GenericItem
{
    public static final String RESTRICTIONTYPE = "restrictionType";
    public static final String DESCRIPTIONPATTERN = "descriptionPattern";
    public static final String RENDEREDDESCRIPTION = "renderedDescription";
    public static final String PROMOTION = "promotion";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("descriptionPattern", Item.AttributeMode.INITIAL);
        tmp.put("promotion", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getDescriptionPattern(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractPromotionRestriction.getDescriptionPattern requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "descriptionPattern");
    }


    public String getDescriptionPattern()
    {
        return getDescriptionPattern(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescriptionPattern(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "descriptionPattern", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescriptionPattern()
    {
        return getAllDescriptionPattern(getSession().getSessionContext());
    }


    public void setDescriptionPattern(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractPromotionRestriction.setDescriptionPattern requires a session language", 0);
        }
        setLocalizedProperty(ctx, "descriptionPattern", value);
    }


    public void setDescriptionPattern(String value)
    {
        setDescriptionPattern(getSession().getSessionContext(), value);
    }


    public void setAllDescriptionPattern(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "descriptionPattern", value);
    }


    public void setAllDescriptionPattern(Map<Language, String> value)
    {
        setAllDescriptionPattern(getSession().getSessionContext(), value);
    }


    public AbstractPromotion getPromotion(SessionContext ctx)
    {
        return (AbstractPromotion)getProperty(ctx, "promotion");
    }


    public AbstractPromotion getPromotion()
    {
        return getPromotion(getSession().getSessionContext());
    }


    public void setPromotion(SessionContext ctx, AbstractPromotion value)
    {
        setProperty(ctx, "promotion", value);
    }


    public void setPromotion(AbstractPromotion value)
    {
        setPromotion(getSession().getSessionContext(), value);
    }


    public String getRenderedDescription()
    {
        return getRenderedDescription(getSession().getSessionContext());
    }


    public String getRestrictionType()
    {
        return getRestrictionType(getSession().getSessionContext());
    }


    public Map<Language, String> getAllRestrictionType()
    {
        return getAllRestrictionType(getSession().getSessionContext());
    }


    public abstract String getRenderedDescription(SessionContext paramSessionContext);


    public abstract String getRestrictionType(SessionContext paramSessionContext);


    public abstract Map<Language, String> getAllRestrictionType(SessionContext paramSessionContext);
}
