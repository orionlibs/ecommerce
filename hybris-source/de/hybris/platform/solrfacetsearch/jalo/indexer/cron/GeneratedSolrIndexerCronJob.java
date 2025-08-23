package de.hybris.platform.solrfacetsearch.jalo.indexer.cron;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrIndexerCronJob extends CronJob
{
    public static final String INDEXEROPERATION = "indexerOperation";
    public static final String INDEXERHINTS = "indexerHints";
    public static final String FACETSEARCHCONFIGPOS = "facetSearchConfigPOS";
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrIndexerCronJob> FACETSEARCHCONFIGHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXERCRONJOB, false, "facetSearchConfig", "facetSearchConfigPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("indexerOperation", Item.AttributeMode.INITIAL);
        tmp.put("indexerHints", Item.AttributeMode.INITIAL);
        tmp.put("facetSearchConfigPOS", Item.AttributeMode.INITIAL);
        tmp.put("facetSearchConfig", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        FACETSEARCHCONFIGHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
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
        FACETSEARCHCONFIGHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setFacetSearchConfig(SolrFacetSearchConfig value)
    {
        setFacetSearchConfig(getSession().getSessionContext(), value);
    }


    Integer getFacetSearchConfigPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "facetSearchConfigPOS");
    }


    Integer getFacetSearchConfigPOS()
    {
        return getFacetSearchConfigPOS(getSession().getSessionContext());
    }


    int getFacetSearchConfigPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getFacetSearchConfigPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getFacetSearchConfigPOSAsPrimitive()
    {
        return getFacetSearchConfigPOSAsPrimitive(getSession().getSessionContext());
    }


    void setFacetSearchConfigPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "facetSearchConfigPOS", value);
    }


    void setFacetSearchConfigPOS(Integer value)
    {
        setFacetSearchConfigPOS(getSession().getSessionContext(), value);
    }


    void setFacetSearchConfigPOS(SessionContext ctx, int value)
    {
        setFacetSearchConfigPOS(ctx, Integer.valueOf(value));
    }


    void setFacetSearchConfigPOS(int value)
    {
        setFacetSearchConfigPOS(getSession().getSessionContext(), value);
    }


    public Map<String, String> getAllIndexerHints(SessionContext ctx)
    {
        Map<String, String> map = (Map<String, String>)getProperty(ctx, "indexerHints");
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, String> getAllIndexerHints()
    {
        return getAllIndexerHints(getSession().getSessionContext());
    }


    public void setAllIndexerHints(SessionContext ctx, Map<String, String> value)
    {
        setProperty(ctx, "indexerHints", value);
    }


    public void setAllIndexerHints(Map<String, String> value)
    {
        setAllIndexerHints(getSession().getSessionContext(), value);
    }


    public EnumerationValue getIndexerOperation(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "indexerOperation");
    }


    public EnumerationValue getIndexerOperation()
    {
        return getIndexerOperation(getSession().getSessionContext());
    }


    public void setIndexerOperation(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "indexerOperation", value);
    }


    public void setIndexerOperation(EnumerationValue value)
    {
        setIndexerOperation(getSession().getSessionContext(), value);
    }
}
