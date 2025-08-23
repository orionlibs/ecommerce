package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedFlexibleSearchCronJob extends MediaProcessCronJob
{
    public static final String QUERY = "query";
    public static final String FAILONUNKNOWN = "failOnUnknown";
    public static final String DONTNEEDTOTAL = "dontNeedTotal";
    public static final String RANGESTART = "rangeStart";
    public static final String COUNT = "count";
    public static final String SEARCHRESULT = "searchResult";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(MediaProcessCronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("query", Item.AttributeMode.INITIAL);
        tmp.put("failOnUnknown", Item.AttributeMode.INITIAL);
        tmp.put("dontNeedTotal", Item.AttributeMode.INITIAL);
        tmp.put("rangeStart", Item.AttributeMode.INITIAL);
        tmp.put("count", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "count");
    }


    public Integer getCount()
    {
        return getCount(getSession().getSessionContext());
    }


    public int getCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getCountAsPrimitive()
    {
        return getCountAsPrimitive(getSession().getSessionContext());
    }


    public void setCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "count", value);
    }


    public void setCount(Integer value)
    {
        setCount(getSession().getSessionContext(), value);
    }


    public void setCount(SessionContext ctx, int value)
    {
        setCount(ctx, Integer.valueOf(value));
    }


    public void setCount(int value)
    {
        setCount(getSession().getSessionContext(), value);
    }


    public Boolean isDontNeedTotal(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "dontNeedTotal");
    }


    public Boolean isDontNeedTotal()
    {
        return isDontNeedTotal(getSession().getSessionContext());
    }


    public boolean isDontNeedTotalAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDontNeedTotal(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDontNeedTotalAsPrimitive()
    {
        return isDontNeedTotalAsPrimitive(getSession().getSessionContext());
    }


    public void setDontNeedTotal(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "dontNeedTotal", value);
    }


    public void setDontNeedTotal(Boolean value)
    {
        setDontNeedTotal(getSession().getSessionContext(), value);
    }


    public void setDontNeedTotal(SessionContext ctx, boolean value)
    {
        setDontNeedTotal(ctx, Boolean.valueOf(value));
    }


    public void setDontNeedTotal(boolean value)
    {
        setDontNeedTotal(getSession().getSessionContext(), value);
    }


    public Boolean isFailOnUnknown(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "failOnUnknown");
    }


    public Boolean isFailOnUnknown()
    {
        return isFailOnUnknown(getSession().getSessionContext());
    }


    public boolean isFailOnUnknownAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFailOnUnknown(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFailOnUnknownAsPrimitive()
    {
        return isFailOnUnknownAsPrimitive(getSession().getSessionContext());
    }


    public void setFailOnUnknown(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "failOnUnknown", value);
    }


    public void setFailOnUnknown(Boolean value)
    {
        setFailOnUnknown(getSession().getSessionContext(), value);
    }


    public void setFailOnUnknown(SessionContext ctx, boolean value)
    {
        setFailOnUnknown(ctx, Boolean.valueOf(value));
    }


    public void setFailOnUnknown(boolean value)
    {
        setFailOnUnknown(getSession().getSessionContext(), value);
    }


    public String getQuery(SessionContext ctx)
    {
        return (String)getProperty(ctx, "query");
    }


    public String getQuery()
    {
        return getQuery(getSession().getSessionContext());
    }


    public void setQuery(SessionContext ctx, String value)
    {
        setProperty(ctx, "query", value);
    }


    public void setQuery(String value)
    {
        setQuery(getSession().getSessionContext(), value);
    }


    public Integer getRangeStart(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "rangeStart");
    }


    public Integer getRangeStart()
    {
        return getRangeStart(getSession().getSessionContext());
    }


    public int getRangeStartAsPrimitive(SessionContext ctx)
    {
        Integer value = getRangeStart(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getRangeStartAsPrimitive()
    {
        return getRangeStartAsPrimitive(getSession().getSessionContext());
    }


    public void setRangeStart(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "rangeStart", value);
    }


    public void setRangeStart(Integer value)
    {
        setRangeStart(getSession().getSessionContext(), value);
    }


    public void setRangeStart(SessionContext ctx, int value)
    {
        setRangeStart(ctx, Integer.valueOf(value));
    }


    public void setRangeStart(int value)
    {
        setRangeStart(getSession().getSessionContext(), value);
    }


    public Collection<String> getSearchResult()
    {
        return getSearchResult(getSession().getSessionContext());
    }


    public abstract Collection<String> getSearchResult(SessionContext paramSessionContext);
}
