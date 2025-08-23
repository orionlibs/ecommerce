package de.hybris.platform.jalo.initialization;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.User;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSystemSetupAudit extends GenericItem
{
    public static final String HASH = "hash";
    public static final String EXTENSIONNAME = "extensionName";
    public static final String REQUIRED = "required";
    public static final String PATCH = "patch";
    public static final String USER = "user";
    public static final String NAME = "name";
    public static final String CLASSNAME = "className";
    public static final String METHODNAME = "methodName";
    public static final String DESCRIPTION = "description";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("hash", Item.AttributeMode.INITIAL);
        tmp.put("extensionName", Item.AttributeMode.INITIAL);
        tmp.put("required", Item.AttributeMode.INITIAL);
        tmp.put("patch", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("className", Item.AttributeMode.INITIAL);
        tmp.put("methodName", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getClassName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "className");
    }


    public String getClassName()
    {
        return getClassName(getSession().getSessionContext());
    }


    protected void setClassName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'className' is not changeable", 0);
        }
        setProperty(ctx, "className", value);
    }


    protected void setClassName(String value)
    {
        setClassName(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public String getExtensionName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "extensionName");
    }


    public String getExtensionName()
    {
        return getExtensionName(getSession().getSessionContext());
    }


    protected void setExtensionName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'extensionName' is not changeable", 0);
        }
        setProperty(ctx, "extensionName", value);
    }


    protected void setExtensionName(String value)
    {
        setExtensionName(getSession().getSessionContext(), value);
    }


    public String getHash(SessionContext ctx)
    {
        return (String)getProperty(ctx, "hash");
    }


    public String getHash()
    {
        return getHash(getSession().getSessionContext());
    }


    protected void setHash(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'hash' is not changeable", 0);
        }
        setProperty(ctx, "hash", value);
    }


    protected void setHash(String value)
    {
        setHash(getSession().getSessionContext(), value);
    }


    public String getMethodName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "methodName");
    }


    public String getMethodName()
    {
        return getMethodName(getSession().getSessionContext());
    }


    protected void setMethodName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'methodName' is not changeable", 0);
        }
        setProperty(ctx, "methodName", value);
    }


    protected void setMethodName(String value)
    {
        setMethodName(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    protected void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'name' is not changeable", 0);
        }
        setProperty(ctx, "name", value);
    }


    protected void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public Boolean isPatch(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "patch");
    }


    public Boolean isPatch()
    {
        return isPatch(getSession().getSessionContext());
    }


    public boolean isPatchAsPrimitive(SessionContext ctx)
    {
        Boolean value = isPatch(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isPatchAsPrimitive()
    {
        return isPatchAsPrimitive(getSession().getSessionContext());
    }


    protected void setPatch(SessionContext ctx, Boolean value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'patch' is not changeable", 0);
        }
        setProperty(ctx, "patch", value);
    }


    protected void setPatch(Boolean value)
    {
        setPatch(getSession().getSessionContext(), value);
    }


    protected void setPatch(SessionContext ctx, boolean value)
    {
        setPatch(ctx, Boolean.valueOf(value));
    }


    protected void setPatch(boolean value)
    {
        setPatch(getSession().getSessionContext(), value);
    }


    public Boolean isRequired(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "required");
    }


    public Boolean isRequired()
    {
        return isRequired(getSession().getSessionContext());
    }


    public boolean isRequiredAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRequired(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRequiredAsPrimitive()
    {
        return isRequiredAsPrimitive(getSession().getSessionContext());
    }


    protected void setRequired(SessionContext ctx, Boolean value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'required' is not changeable", 0);
        }
        setProperty(ctx, "required", value);
    }


    protected void setRequired(Boolean value)
    {
        setRequired(getSession().getSessionContext(), value);
    }


    protected void setRequired(SessionContext ctx, boolean value)
    {
        setRequired(ctx, Boolean.valueOf(value));
    }


    protected void setRequired(boolean value)
    {
        setRequired(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    protected void setUser(SessionContext ctx, User value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'user' is not changeable", 0);
        }
        setProperty(ctx, "user", value);
    }


    protected void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }
}
