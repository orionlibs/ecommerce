package de.hybris.platform.scripting.jalo;

import de.hybris.platform.jalo.AbstractDynamicContent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedScript extends AbstractDynamicContent
{
    public static final String DESCRIPTION = "description";
    public static final String SCRIPTTYPE = "scriptType";
    public static final String AUTODISABLING = "autodisabling";
    public static final String DISABLED = "disabled";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractDynamicContent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("scriptType", Item.AttributeMode.INITIAL);
        tmp.put("autodisabling", Item.AttributeMode.INITIAL);
        tmp.put("disabled", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAutodisabling(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "autodisabling");
    }


    public Boolean isAutodisabling()
    {
        return isAutodisabling(getSession().getSessionContext());
    }


    public boolean isAutodisablingAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAutodisabling(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAutodisablingAsPrimitive()
    {
        return isAutodisablingAsPrimitive(getSession().getSessionContext());
    }


    public void setAutodisabling(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "autodisabling", value);
    }


    public void setAutodisabling(Boolean value)
    {
        setAutodisabling(getSession().getSessionContext(), value);
    }


    public void setAutodisabling(SessionContext ctx, boolean value)
    {
        setAutodisabling(ctx, Boolean.valueOf(value));
    }


    public void setAutodisabling(boolean value)
    {
        setAutodisabling(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedScript.getDescription requires a session language", 0);
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
            throw new JaloInvalidParameterException("GeneratedScript.setDescription requires a session language", 0);
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


    public Boolean isDisabled(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "disabled");
    }


    public Boolean isDisabled()
    {
        return isDisabled(getSession().getSessionContext());
    }


    public boolean isDisabledAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDisabled(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDisabledAsPrimitive()
    {
        return isDisabledAsPrimitive(getSession().getSessionContext());
    }


    public void setDisabled(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "disabled", value);
    }


    public void setDisabled(Boolean value)
    {
        setDisabled(getSession().getSessionContext(), value);
    }


    public void setDisabled(SessionContext ctx, boolean value)
    {
        setDisabled(ctx, Boolean.valueOf(value));
    }


    public void setDisabled(boolean value)
    {
        setDisabled(getSession().getSessionContext(), value);
    }


    public EnumerationValue getScriptType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "scriptType");
    }


    public EnumerationValue getScriptType()
    {
        return getScriptType(getSession().getSessionContext());
    }


    public void setScriptType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "scriptType", value);
    }


    public void setScriptType(EnumerationValue value)
    {
        setScriptType(getSession().getSessionContext(), value);
    }
}
