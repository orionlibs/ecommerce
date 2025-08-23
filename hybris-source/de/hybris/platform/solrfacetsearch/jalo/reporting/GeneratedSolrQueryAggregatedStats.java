package de.hybris.platform.solrfacetsearch.jalo.reporting;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrQueryAggregatedStats extends GenericItem
{
    public static final String TIME = "time";
    public static final String INDEXCONFIG = "indexConfig";
    public static final String LANGUAGE = "language";
    public static final String QUERY = "query";
    public static final String COUNT = "count";
    public static final String AVGNUMBEROFRESULTS = "avgNumberOfResults";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("time", Item.AttributeMode.INITIAL);
        tmp.put("indexConfig", Item.AttributeMode.INITIAL);
        tmp.put("language", Item.AttributeMode.INITIAL);
        tmp.put("query", Item.AttributeMode.INITIAL);
        tmp.put("count", Item.AttributeMode.INITIAL);
        tmp.put("avgNumberOfResults", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Double getAvgNumberOfResults(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "avgNumberOfResults");
    }


    public Double getAvgNumberOfResults()
    {
        return getAvgNumberOfResults(getSession().getSessionContext());
    }


    public double getAvgNumberOfResultsAsPrimitive(SessionContext ctx)
    {
        Double value = getAvgNumberOfResults(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getAvgNumberOfResultsAsPrimitive()
    {
        return getAvgNumberOfResultsAsPrimitive(getSession().getSessionContext());
    }


    public void setAvgNumberOfResults(SessionContext ctx, Double value)
    {
        setProperty(ctx, "avgNumberOfResults", value);
    }


    public void setAvgNumberOfResults(Double value)
    {
        setAvgNumberOfResults(getSession().getSessionContext(), value);
    }


    public void setAvgNumberOfResults(SessionContext ctx, double value)
    {
        setAvgNumberOfResults(ctx, Double.valueOf(value));
    }


    public void setAvgNumberOfResults(double value)
    {
        setAvgNumberOfResults(getSession().getSessionContext(), value);
    }


    public Long getCount(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "count");
    }


    public Long getCount()
    {
        return getCount(getSession().getSessionContext());
    }


    public long getCountAsPrimitive(SessionContext ctx)
    {
        Long value = getCount(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getCountAsPrimitive()
    {
        return getCountAsPrimitive(getSession().getSessionContext());
    }


    public void setCount(SessionContext ctx, Long value)
    {
        setProperty(ctx, "count", value);
    }


    public void setCount(Long value)
    {
        setCount(getSession().getSessionContext(), value);
    }


    public void setCount(SessionContext ctx, long value)
    {
        setCount(ctx, Long.valueOf(value));
    }


    public void setCount(long value)
    {
        setCount(getSession().getSessionContext(), value);
    }


    public SolrFacetSearchConfig getIndexConfig(SessionContext ctx)
    {
        return (SolrFacetSearchConfig)getProperty(ctx, "indexConfig");
    }


    public SolrFacetSearchConfig getIndexConfig()
    {
        return getIndexConfig(getSession().getSessionContext());
    }


    public void setIndexConfig(SessionContext ctx, SolrFacetSearchConfig value)
    {
        setProperty(ctx, "indexConfig", value);
    }


    public void setIndexConfig(SolrFacetSearchConfig value)
    {
        setIndexConfig(getSession().getSessionContext(), value);
    }


    public Language getLanguage(SessionContext ctx)
    {
        return (Language)getProperty(ctx, "language");
    }


    public Language getLanguage()
    {
        return getLanguage(getSession().getSessionContext());
    }


    public void setLanguage(SessionContext ctx, Language value)
    {
        setProperty(ctx, "language", value);
    }


    public void setLanguage(Language value)
    {
        setLanguage(getSession().getSessionContext(), value);
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


    public Date getTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "time");
    }


    public Date getTime()
    {
        return getTime(getSession().getSessionContext());
    }


    public void setTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "time", value);
    }


    public void setTime(Date value)
    {
        setTime(getSession().getSessionContext(), value);
    }
}
