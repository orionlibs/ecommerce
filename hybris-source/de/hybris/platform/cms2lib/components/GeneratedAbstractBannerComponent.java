package de.hybris.platform.cms2lib.components;

import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractBannerComponent extends SimpleCMSComponent
{
    public static final String MEDIA = "media";
    public static final String URLLINK = "urlLink";
    public static final String EXTERNAL = "external";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("media", Item.AttributeMode.INITIAL);
        tmp.put("urlLink", Item.AttributeMode.INITIAL);
        tmp.put("external", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isExternal(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "external");
    }


    public Boolean isExternal()
    {
        return isExternal(getSession().getSessionContext());
    }


    public boolean isExternalAsPrimitive(SessionContext ctx)
    {
        Boolean value = isExternal(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isExternalAsPrimitive()
    {
        return isExternalAsPrimitive(getSession().getSessionContext());
    }


    public void setExternal(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "external", value);
    }


    public void setExternal(Boolean value)
    {
        setExternal(getSession().getSessionContext(), value);
    }


    public void setExternal(SessionContext ctx, boolean value)
    {
        setExternal(ctx, Boolean.valueOf(value));
    }


    public void setExternal(boolean value)
    {
        setExternal(getSession().getSessionContext(), value);
    }


    public Media getMedia(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractBannerComponent.getMedia requires a session language", 0);
        }
        return (Media)getLocalizedProperty(ctx, "media");
    }


    public Media getMedia()
    {
        return getMedia(getSession().getSessionContext());
    }


    public Map<Language, Media> getAllMedia(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "media", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, Media> getAllMedia()
    {
        return getAllMedia(getSession().getSessionContext());
    }


    public void setMedia(SessionContext ctx, Media value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedAbstractBannerComponent.setMedia requires a session language", 0);
        }
        setLocalizedProperty(ctx, "media", value);
    }


    public void setMedia(Media value)
    {
        setMedia(getSession().getSessionContext(), value);
    }


    public void setAllMedia(SessionContext ctx, Map<Language, Media> value)
    {
        setAllLocalizedProperties(ctx, "media", value);
    }


    public void setAllMedia(Map<Language, Media> value)
    {
        setAllMedia(getSession().getSessionContext(), value);
    }


    public String getUrlLink(SessionContext ctx)
    {
        return (String)getProperty(ctx, "urlLink");
    }


    public String getUrlLink()
    {
        return getUrlLink(getSession().getSessionContext());
    }


    public void setUrlLink(SessionContext ctx, String value)
    {
        setProperty(ctx, "urlLink", value);
    }


    public void setUrlLink(String value)
    {
        setUrlLink(getSession().getSessionContext(), value);
    }
}
