package de.hybris.platform.cms2.jalo.contents.components;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCMSParagraphComponent extends SimpleCMSComponent
{
    public static final String CONTENT = "content";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("content", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getContent(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCMSParagraphComponent.getContent requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "content");
    }


    public String getContent()
    {
        return getContent(getSession().getSessionContext());
    }


    public Map<Language, String> getAllContent(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "content", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllContent()
    {
        return getAllContent(getSession().getSessionContext());
    }


    public void setContent(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCMSParagraphComponent.setContent requires a session language", 0);
        }
        setLocalizedProperty(ctx, "content", value);
    }


    public void setContent(String value)
    {
        setContent(getSession().getSessionContext(), value);
    }


    public void setAllContent(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "content", value);
    }


    public void setAllContent(Map<Language, String> value)
    {
        setAllContent(getSession().getSessionContext(), value);
    }
}
