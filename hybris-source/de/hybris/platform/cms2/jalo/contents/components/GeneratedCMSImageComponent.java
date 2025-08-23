package de.hybris.platform.cms2.jalo.contents.components;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCMSImageComponent extends SimpleCMSComponent
{
    public static final String MEDIA = "media";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("media", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Media getMedia(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCMSImageComponent.getMedia requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedCMSImageComponent.setMedia requires a session language", 0);
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
}
