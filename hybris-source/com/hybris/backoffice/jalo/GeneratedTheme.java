package com.hybris.backoffice.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedTheme extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String THUMBNAIL = "thumbnail";
    public static final String STYLE = "style";
    public static final String SEQUENCE = "sequence";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("thumbnail", Item.AttributeMode.INITIAL);
        tmp.put("style", Item.AttributeMode.INITIAL);
        tmp.put("sequence", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedTheme.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedTheme.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public Integer getSequence(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "sequence");
    }


    public Integer getSequence()
    {
        return getSequence(getSession().getSessionContext());
    }


    public int getSequenceAsPrimitive(SessionContext ctx)
    {
        Integer value = getSequence(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getSequenceAsPrimitive()
    {
        return getSequenceAsPrimitive(getSession().getSessionContext());
    }


    public void setSequence(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "sequence", value);
    }


    public void setSequence(Integer value)
    {
        setSequence(getSession().getSessionContext(), value);
    }


    public void setSequence(SessionContext ctx, int value)
    {
        setSequence(ctx, Integer.valueOf(value));
    }


    public void setSequence(int value)
    {
        setSequence(getSession().getSessionContext(), value);
    }


    public Media getStyle(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "style");
    }


    public Media getStyle()
    {
        return getStyle(getSession().getSessionContext());
    }


    public void setStyle(SessionContext ctx, Media value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setStyle(Media value)
    {
        setStyle(getSession().getSessionContext(), value);
    }


    public Media getThumbnail(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "thumbnail");
    }


    public Media getThumbnail()
    {
        return getThumbnail(getSession().getSessionContext());
    }


    public void setThumbnail(SessionContext ctx, Media value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setThumbnail(Media value)
    {
        setThumbnail(getSession().getSessionContext(), value);
    }
}
