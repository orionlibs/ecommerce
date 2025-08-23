package de.hybris.platform.solrfacetsearch.jalo.indexer.cron;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrExtIndexerCronJob extends SolrIndexerCronJob
{
    public static final String INDEXEDTYPE = "indexedType";
    public static final String INDEXEDPROPERTIES = "indexedProperties";
    public static final String QUERY = "query";
    public static final String QUERYPARAMETERPROVIDER = "queryParameterProvider";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SolrIndexerCronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("indexedType", Item.AttributeMode.INITIAL);
        tmp.put("indexedProperties", Item.AttributeMode.INITIAL);
        tmp.put("query", Item.AttributeMode.INITIAL);
        tmp.put("queryParameterProvider", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<String> getIndexedProperties(SessionContext ctx)
    {
        Collection<String> coll = (Collection<String>)getProperty(ctx, "indexedProperties");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<String> getIndexedProperties()
    {
        return getIndexedProperties(getSession().getSessionContext());
    }


    public void setIndexedProperties(SessionContext ctx, Collection<String> value)
    {
        setProperty(ctx, "indexedProperties", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setIndexedProperties(Collection<String> value)
    {
        setIndexedProperties(getSession().getSessionContext(), value);
    }


    public String getIndexedType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "indexedType");
    }


    public String getIndexedType()
    {
        return getIndexedType(getSession().getSessionContext());
    }


    public void setIndexedType(SessionContext ctx, String value)
    {
        setProperty(ctx, "indexedType", value);
    }


    public void setIndexedType(String value)
    {
        setIndexedType(getSession().getSessionContext(), value);
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


    public String getQueryParameterProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "queryParameterProvider");
    }


    public String getQueryParameterProvider()
    {
        return getQueryParameterProvider(getSession().getSessionContext());
    }


    public void setQueryParameterProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "queryParameterProvider", value);
    }


    public void setQueryParameterProvider(String value)
    {
        setQueryParameterProvider(getSession().getSessionContext(), value);
    }
}
