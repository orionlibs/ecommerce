package de.hybris.platform.solrfacetsearch.jalo.indexer.cron;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrIndexOptimizationCronJob extends CronJob
{
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("facetSearchConfig", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public SolrFacetSearchConfig getFacetSearchConfig(SessionContext ctx)
    {
        return (SolrFacetSearchConfig)getProperty(ctx, "facetSearchConfig");
    }


    public SolrFacetSearchConfig getFacetSearchConfig()
    {
        return getFacetSearchConfig(getSession().getSessionContext());
    }


    public void setFacetSearchConfig(SessionContext ctx, SolrFacetSearchConfig value)
    {
        setProperty(ctx, "facetSearchConfig", value);
    }


    public void setFacetSearchConfig(SolrFacetSearchConfig value)
    {
        setFacetSearchConfig(getSession().getSessionContext(), value);
    }
}
