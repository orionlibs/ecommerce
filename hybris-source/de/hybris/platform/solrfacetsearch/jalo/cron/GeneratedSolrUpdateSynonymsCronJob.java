package de.hybris.platform.solrfacetsearch.jalo.cron;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrUpdateSynonymsCronJob extends CronJob
{
    public static final String LANGUAGE = "language";
    public static final String SOLRFACETSEARCHCONFIGPOS = "solrFacetSearchConfigPOS";
    public static final String SOLRFACETSEARCHCONFIG = "solrFacetSearchConfig";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrUpdateSynonymsCronJob> SOLRFACETSEARCHCONFIGHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRUPDATESYNONYMSCRONJOB, false, "solrFacetSearchConfig", "solrFacetSearchConfigPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("language", Item.AttributeMode.INITIAL);
        tmp.put("solrFacetSearchConfigPOS", Item.AttributeMode.INITIAL);
        tmp.put("solrFacetSearchConfig", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SOLRFACETSEARCHCONFIGHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
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


    public SolrFacetSearchConfig getSolrFacetSearchConfig(SessionContext ctx)
    {
        return (SolrFacetSearchConfig)getProperty(ctx, "solrFacetSearchConfig");
    }


    public SolrFacetSearchConfig getSolrFacetSearchConfig()
    {
        return getSolrFacetSearchConfig(getSession().getSessionContext());
    }


    public void setSolrFacetSearchConfig(SessionContext ctx, SolrFacetSearchConfig value)
    {
        SOLRFACETSEARCHCONFIGHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSolrFacetSearchConfig(SolrFacetSearchConfig value)
    {
        setSolrFacetSearchConfig(getSession().getSessionContext(), value);
    }


    Integer getSolrFacetSearchConfigPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "solrFacetSearchConfigPOS");
    }


    Integer getSolrFacetSearchConfigPOS()
    {
        return getSolrFacetSearchConfigPOS(getSession().getSessionContext());
    }


    int getSolrFacetSearchConfigPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getSolrFacetSearchConfigPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getSolrFacetSearchConfigPOSAsPrimitive()
    {
        return getSolrFacetSearchConfigPOSAsPrimitive(getSession().getSessionContext());
    }


    void setSolrFacetSearchConfigPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "solrFacetSearchConfigPOS", value);
    }


    void setSolrFacetSearchConfigPOS(Integer value)
    {
        setSolrFacetSearchConfigPOS(getSession().getSessionContext(), value);
    }


    void setSolrFacetSearchConfigPOS(SessionContext ctx, int value)
    {
        setSolrFacetSearchConfigPOS(ctx, Integer.valueOf(value));
    }


    void setSolrFacetSearchConfigPOS(int value)
    {
        setSolrFacetSearchConfigPOS(getSession().getSessionContext(), value);
    }
}
