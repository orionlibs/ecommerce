package de.hybris.platform.impex.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.processing.jalo.Batch;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedImportBatch extends Batch
{
    public static final String GROUP = "group";
    public static final String METADATA = "metadata";
    public static final String IMPORTCONTENTCODE = "importContentCode";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Batch.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("group", Item.AttributeMode.INITIAL);
        tmp.put("metadata", Item.AttributeMode.INITIAL);
        tmp.put("importContentCode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getGroup(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "group");
    }


    public Integer getGroup()
    {
        return getGroup(getSession().getSessionContext());
    }


    public int getGroupAsPrimitive(SessionContext ctx)
    {
        Integer value = getGroup(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getGroupAsPrimitive()
    {
        return getGroupAsPrimitive(getSession().getSessionContext());
    }


    protected void setGroup(SessionContext ctx, Integer value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'group' is not changeable", 0);
        }
        setProperty(ctx, "group", value);
    }


    protected void setGroup(Integer value)
    {
        setGroup(getSession().getSessionContext(), value);
    }


    protected void setGroup(SessionContext ctx, int value)
    {
        setGroup(ctx, Integer.valueOf(value));
    }


    protected void setGroup(int value)
    {
        setGroup(getSession().getSessionContext(), value);
    }


    public String getImportContentCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "importContentCode");
    }


    public String getImportContentCode()
    {
        return getImportContentCode(getSession().getSessionContext());
    }


    protected void setImportContentCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'importContentCode' is not changeable", 0);
        }
        setProperty(ctx, "importContentCode", value);
    }


    protected void setImportContentCode(String value)
    {
        setImportContentCode(getSession().getSessionContext(), value);
    }


    public String getMetadata(SessionContext ctx)
    {
        return (String)getProperty(ctx, "metadata");
    }


    public String getMetadata()
    {
        return getMetadata(getSession().getSessionContext());
    }


    protected void setMetadata(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'metadata' is not changeable", 0);
        }
        setProperty(ctx, "metadata", value);
    }


    protected void setMetadata(String value)
    {
        setMetadata(getSession().getSessionContext(), value);
    }
}
