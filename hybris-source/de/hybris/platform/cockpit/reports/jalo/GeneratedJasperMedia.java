package de.hybris.platform.cockpit.reports.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedJasperMedia extends Media
{
    public static final String TITLE = "title";
    public static final String REPORTDESCRIPTION = "reportDescription";
    public static final String ICON = "icon";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Media.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("title", Item.AttributeMode.INITIAL);
        tmp.put("reportDescription", Item.AttributeMode.INITIAL);
        tmp.put("icon", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Media getIcon(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "icon");
    }


    public Media getIcon()
    {
        return getIcon(getSession().getSessionContext());
    }


    public void setIcon(SessionContext ctx, Media value)
    {
        setProperty(ctx, "icon", value);
    }


    public void setIcon(Media value)
    {
        setIcon(getSession().getSessionContext(), value);
    }


    public String getReportDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedJasperMedia.getReportDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "reportDescription");
    }


    public String getReportDescription()
    {
        return getReportDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllReportDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "reportDescription", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllReportDescription()
    {
        return getAllReportDescription(getSession().getSessionContext());
    }


    public void setReportDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedJasperMedia.setReportDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "reportDescription", value);
    }


    public void setReportDescription(String value)
    {
        setReportDescription(getSession().getSessionContext(), value);
    }


    public void setAllReportDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "reportDescription", value);
    }


    public void setAllReportDescription(Map<Language, String> value)
    {
        setAllReportDescription(getSession().getSessionContext(), value);
    }


    public String getTitle(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedJasperMedia.getTitle requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "title");
    }


    public String getTitle()
    {
        return getTitle(getSession().getSessionContext());
    }


    public Map<Language, String> getAllTitle(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "title", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllTitle()
    {
        return getAllTitle(getSession().getSessionContext());
    }


    public void setTitle(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedJasperMedia.setTitle requires a session language", 0);
        }
        setLocalizedProperty(ctx, "title", value);
    }


    public void setTitle(String value)
    {
        setTitle(getSession().getSessionContext(), value);
    }


    public void setAllTitle(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "title", value);
    }


    public void setAllTitle(Map<Language, String> value)
    {
        setAllTitle(getSession().getSessionContext(), value);
    }
}
