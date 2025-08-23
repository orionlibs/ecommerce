package de.hybris.platform.solrfacetsearch.jalo.indexer.cron;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrIndexerHotUpdateCronJob extends SolrIndexerCronJob
{
    public static final String INDEXTYPENAME = "indexTypeName";
    public static final String ITEMS = "items";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SolrIndexerCronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("indexTypeName", Item.AttributeMode.INITIAL);
        tmp.put("items", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getIndexTypeName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "indexTypeName");
    }


    public String getIndexTypeName()
    {
        return getIndexTypeName(getSession().getSessionContext());
    }


    public void setIndexTypeName(SessionContext ctx, String value)
    {
        setProperty(ctx, "indexTypeName", value);
    }


    public void setIndexTypeName(String value)
    {
        setIndexTypeName(getSession().getSessionContext(), value);
    }


    public Collection<Item> getItems(SessionContext ctx)
    {
        Collection<Item> coll = (Collection<Item>)getProperty(ctx, "items");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Item> getItems()
    {
        return getItems(getSession().getSessionContext());
    }


    public void setItems(SessionContext ctx, Collection<Item> value)
    {
        setProperty(ctx, "items", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setItems(Collection<Item> value)
    {
        setItems(getSession().getSessionContext(), value);
    }
}
