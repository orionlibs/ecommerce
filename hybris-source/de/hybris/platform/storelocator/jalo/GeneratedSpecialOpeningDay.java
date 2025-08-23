package de.hybris.platform.storelocator.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSpecialOpeningDay extends OpeningDay
{
    public static final String DATE = "date";
    public static final String CLOSED = "closed";
    public static final String NAME = "name";
    public static final String MESSAGE = "message";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(OpeningDay.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("date", Item.AttributeMode.INITIAL);
        tmp.put("closed", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("message", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isClosed(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "closed");
    }


    public Boolean isClosed()
    {
        return isClosed(getSession().getSessionContext());
    }


    public boolean isClosedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isClosed(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isClosedAsPrimitive()
    {
        return isClosedAsPrimitive(getSession().getSessionContext());
    }


    public void setClosed(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "closed", value);
    }


    public void setClosed(Boolean value)
    {
        setClosed(getSession().getSessionContext(), value);
    }


    public void setClosed(SessionContext ctx, boolean value)
    {
        setClosed(ctx, Boolean.valueOf(value));
    }


    public void setClosed(boolean value)
    {
        setClosed(getSession().getSessionContext(), value);
    }


    public Date getDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "date");
    }


    public Date getDate()
    {
        return getDate(getSession().getSessionContext());
    }


    public void setDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "date", value);
    }


    public void setDate(Date value)
    {
        setDate(getSession().getSessionContext(), value);
    }


    public String getMessage(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedSpecialOpeningDay.getMessage requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "message");
    }


    public String getMessage()
    {
        return getMessage(getSession().getSessionContext());
    }


    public Map<Language, String> getAllMessage(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "message", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllMessage()
    {
        return getAllMessage(getSession().getSessionContext());
    }


    public void setMessage(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedSpecialOpeningDay.setMessage requires a session language", 0);
        }
        setLocalizedProperty(ctx, "message", value);
    }


    public void setMessage(String value)
    {
        setMessage(getSession().getSessionContext(), value);
    }


    public void setAllMessage(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "message", value);
    }


    public void setAllMessage(Map<Language, String> value)
    {
        setAllMessage(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedSpecialOpeningDay.getName requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedSpecialOpeningDay.setName requires a session language", 0);
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
}
