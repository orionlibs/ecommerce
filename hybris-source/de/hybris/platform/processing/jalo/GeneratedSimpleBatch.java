package de.hybris.platform.processing.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSimpleBatch extends Batch
{
    public static final String RESULTBATCHID = "resultBatchId";
    public static final String RETRIES = "retries";
    public static final String SCRIPTCODE = "scriptCode";
    public static final String CONTEXT = "context";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Batch.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("resultBatchId", Item.AttributeMode.INITIAL);
        tmp.put("retries", Item.AttributeMode.INITIAL);
        tmp.put("scriptCode", Item.AttributeMode.INITIAL);
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


    public String getResultBatchId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "resultBatchId");
    }


    public String getResultBatchId()
    {
        return getResultBatchId(getSession().getSessionContext());
    }


    public void setResultBatchId(SessionContext ctx, String value)
    {
        setProperty(ctx, "resultBatchId", value);
    }


    public void setResultBatchId(String value)
    {
        setResultBatchId(getSession().getSessionContext(), value);
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


    public String getScriptCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "scriptCode");
    }


    public String getScriptCode()
    {
        return getScriptCode(getSession().getSessionContext());
    }


    public void setScriptCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "scriptCode", value);
    }


    public void setScriptCode(String value)
    {
        setScriptCode(getSession().getSessionContext(), value);
    }
}
