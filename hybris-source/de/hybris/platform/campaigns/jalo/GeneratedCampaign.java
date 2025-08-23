package de.hybris.platform.campaigns.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCampaign extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    public static final String ENABLED = "enabled";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("startDate", Item.AttributeMode.INITIAL);
        tmp.put("endDate", Item.AttributeMode.INITIAL);
        tmp.put("enabled", Item.AttributeMode.INITIAL);
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


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCampaign.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCampaign.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    public Boolean isEnabled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "enabled");
    }


    public Boolean isEnabled()
    {
        return isEnabled(getSession().getSessionContext());
    }


    public boolean isEnabledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEnabled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEnabledAsPrimitive()
    {
        return isEnabledAsPrimitive(getSession().getSessionContext());
    }


    public void setEnabled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "enabled", value);
    }


    public void setEnabled(Boolean value)
    {
        setEnabled(getSession().getSessionContext(), value);
    }


    public void setEnabled(SessionContext ctx, boolean value)
    {
        setEnabled(ctx, Boolean.valueOf(value));
    }


    public void setEnabled(boolean value)
    {
        setEnabled(getSession().getSessionContext(), value);
    }


    public Date getEndDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "endDate");
    }


    public Date getEndDate()
    {
        return getEndDate(getSession().getSessionContext());
    }


    public void setEndDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "endDate", value);
    }


    public void setEndDate(Date value)
    {
        setEndDate(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCampaign.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedCampaign.setName requires a session language", 0);
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


    public Date getStartDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "startDate");
    }


    public Date getStartDate()
    {
        return getStartDate(getSession().getSessionContext());
    }


    public void setStartDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "startDate", value);
    }


    public void setStartDate(Date value)
    {
        setStartDate(getSession().getSessionContext(), value);
    }
}
