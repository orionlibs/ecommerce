package de.hybris.y2ysync.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.processing.jalo.Batch;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedY2YBatch extends Batch
{
    public static final String FINALIZE = "finalize";
    public static final String RETRIES = "retries";
    public static final String ERROR = "error";
    public static final String CONTEXT = "context";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Batch.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("finalize", Item.AttributeMode.INITIAL);
        tmp.put("retries", Item.AttributeMode.INITIAL);
        tmp.put("error", Item.AttributeMode.INITIAL);
        tmp.put("context", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Object getContext(SessionContext ctx)
    {
        return getProperty(ctx, "context");
    }


    public Object getContext()
    {
        return getContext(getSession().getSessionContext());
    }


    public void setContext(SessionContext ctx, Object value)
    {
        setProperty(ctx, "context", value);
    }


    public void setContext(Object value)
    {
        setContext(getSession().getSessionContext(), value);
    }


    public Boolean isError(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "error");
    }


    public Boolean isError()
    {
        return isError(getSession().getSessionContext());
    }


    public boolean isErrorAsPrimitive(SessionContext ctx)
    {
        Boolean value = isError(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isErrorAsPrimitive()
    {
        return isErrorAsPrimitive(getSession().getSessionContext());
    }


    public void setError(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "error", value);
    }


    public void setError(Boolean value)
    {
        setError(getSession().getSessionContext(), value);
    }


    public void setError(SessionContext ctx, boolean value)
    {
        setError(ctx, Boolean.valueOf(value));
    }


    public void setError(boolean value)
    {
        setError(getSession().getSessionContext(), value);
    }


    public Boolean isFinalize(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "finalize");
    }


    public Boolean isFinalize()
    {
        return isFinalize(getSession().getSessionContext());
    }


    public boolean isFinalizeAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFinalize(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFinalizeAsPrimitive()
    {
        return isFinalizeAsPrimitive(getSession().getSessionContext());
    }


    protected void setFinalize(SessionContext ctx, Boolean value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'finalize' is not changeable", 0);
        }
        setProperty(ctx, "finalize", value);
    }


    protected void setFinalize(Boolean value)
    {
        setFinalize(getSession().getSessionContext(), value);
    }


    protected void setFinalize(SessionContext ctx, boolean value)
    {
        setFinalize(ctx, Boolean.valueOf(value));
    }


    protected void setFinalize(boolean value)
    {
        setFinalize(getSession().getSessionContext(), value);
    }


    public Integer getRetries(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "retries");
    }


    public Integer getRetries()
    {
        return getRetries(getSession().getSessionContext());
    }


    public int getRetriesAsPrimitive(SessionContext ctx)
    {
        Integer value = getRetries(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getRetriesAsPrimitive()
    {
        return getRetriesAsPrimitive(getSession().getSessionContext());
    }


    public void setRetries(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "retries", value);
    }


    public void setRetries(Integer value)
    {
        setRetries(getSession().getSessionContext(), value);
    }


    public void setRetries(SessionContext ctx, int value)
    {
        setRetries(ctx, Integer.valueOf(value));
    }


    public void setRetries(int value)
    {
        setRetries(getSession().getSessionContext(), value);
    }
}
