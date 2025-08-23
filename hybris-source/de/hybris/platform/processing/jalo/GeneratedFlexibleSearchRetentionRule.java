package de.hybris.platform.processing.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedFlexibleSearchRetentionRule extends AbstractRetentionRule
{
    public static final String SEARCHQUERY = "searchQuery";
    public static final String QUERYPARAMETERS = "queryParameters";
    public static final String RETENTIONTIMESECONDS = "retentionTimeSeconds";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRetentionRule.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("searchQuery", Item.AttributeMode.INITIAL);
        tmp.put("queryParameters", Item.AttributeMode.INITIAL);
        tmp.put("retentionTimeSeconds", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Map<String, String> getAllQueryParameters(SessionContext ctx)
    {
        Map<String, String> map = (Map<String, String>)getProperty(ctx, "queryParameters");
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, String> getAllQueryParameters()
    {
        return getAllQueryParameters(getSession().getSessionContext());
    }


    public void setAllQueryParameters(SessionContext ctx, Map<String, String> value)
    {
        setProperty(ctx, "queryParameters", value);
    }


    public void setAllQueryParameters(Map<String, String> value)
    {
        setAllQueryParameters(getSession().getSessionContext(), value);
    }


    public Long getRetentionTimeSeconds(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "retentionTimeSeconds");
    }


    public Long getRetentionTimeSeconds()
    {
        return getRetentionTimeSeconds(getSession().getSessionContext());
    }


    public long getRetentionTimeSecondsAsPrimitive(SessionContext ctx)
    {
        Long value = getRetentionTimeSeconds(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getRetentionTimeSecondsAsPrimitive()
    {
        return getRetentionTimeSecondsAsPrimitive(getSession().getSessionContext());
    }


    public void setRetentionTimeSeconds(SessionContext ctx, Long value)
    {
        setProperty(ctx, "retentionTimeSeconds", value);
    }


    public void setRetentionTimeSeconds(Long value)
    {
        setRetentionTimeSeconds(getSession().getSessionContext(), value);
    }


    public void setRetentionTimeSeconds(SessionContext ctx, long value)
    {
        setRetentionTimeSeconds(ctx, Long.valueOf(value));
    }


    public void setRetentionTimeSeconds(long value)
    {
        setRetentionTimeSeconds(getSession().getSessionContext(), value);
    }


    public String getSearchQuery(SessionContext ctx)
    {
        return (String)getProperty(ctx, "searchQuery");
    }


    public String getSearchQuery()
    {
        return getSearchQuery(getSession().getSessionContext());
    }


    public void setSearchQuery(SessionContext ctx, String value)
    {
        setProperty(ctx, "searchQuery", value);
    }


    public void setSearchQuery(String value)
    {
        setSearchQuery(getSession().getSessionContext(), value);
    }
}
