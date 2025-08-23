package de.hybris.platform.jalo.web;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedStoredHttpSession extends GenericItem
{
    public static final String SESSIONID = "sessionId";
    public static final String CLUSTERID = "clusterId";
    public static final String EXTENSION = "extension";
    public static final String CONTEXTROOT = "contextRoot";
    public static final String SERIALIZEDSESSION = "serializedSession";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("sessionId", Item.AttributeMode.INITIAL);
        tmp.put("clusterId", Item.AttributeMode.INITIAL);
        tmp.put("extension", Item.AttributeMode.INITIAL);
        tmp.put("contextRoot", Item.AttributeMode.INITIAL);
        tmp.put("serializedSession", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getClusterId(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "clusterId");
    }


    public Integer getClusterId()
    {
        return getClusterId(getSession().getSessionContext());
    }


    public int getClusterIdAsPrimitive(SessionContext ctx)
    {
        Integer value = getClusterId(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getClusterIdAsPrimitive()
    {
        return getClusterIdAsPrimitive(getSession().getSessionContext());
    }


    protected void setClusterId(SessionContext ctx, Integer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'clusterId' is not changeable", 0);
        }
        setProperty(ctx, "clusterId", value);
    }


    protected void setClusterId(Integer value)
    {
        setClusterId(getSession().getSessionContext(), value);
    }


    protected void setClusterId(SessionContext ctx, int value)
    {
        setClusterId(ctx, Integer.valueOf(value));
    }


    protected void setClusterId(int value)
    {
        setClusterId(getSession().getSessionContext(), value);
    }


    public String getContextRoot(SessionContext ctx)
    {
        return (String)getProperty(ctx, "contextRoot");
    }


    public String getContextRoot()
    {
        return getContextRoot(getSession().getSessionContext());
    }


    protected void setContextRoot(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'contextRoot' is not changeable", 0);
        }
        setProperty(ctx, "contextRoot", value);
    }


    protected void setContextRoot(String value)
    {
        setContextRoot(getSession().getSessionContext(), value);
    }


    public String getExtension(SessionContext ctx)
    {
        return (String)getProperty(ctx, "extension");
    }


    public String getExtension()
    {
        return getExtension(getSession().getSessionContext());
    }


    protected void setExtension(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'extension' is not changeable", 0);
        }
        setProperty(ctx, "extension", value);
    }


    protected void setExtension(String value)
    {
        setExtension(getSession().getSessionContext(), value);
    }


    public Object getSerializedSession(SessionContext ctx)
    {
        return getProperty(ctx, "serializedSession");
    }


    public Object getSerializedSession()
    {
        return getSerializedSession(getSession().getSessionContext());
    }


    public void setSerializedSession(SessionContext ctx, Object value)
    {
        setProperty(ctx, "serializedSession", value);
    }


    public void setSerializedSession(Object value)
    {
        setSerializedSession(getSession().getSessionContext(), value);
    }


    public String getSessionId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sessionId");
    }


    public String getSessionId()
    {
        return getSessionId(getSession().getSessionContext());
    }


    protected void setSessionId(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'sessionId' is not changeable", 0);
        }
        setProperty(ctx, "sessionId", value);
    }


    protected void setSessionId(String value)
    {
        setSessionId(getSession().getSessionContext(), value);
    }
}
