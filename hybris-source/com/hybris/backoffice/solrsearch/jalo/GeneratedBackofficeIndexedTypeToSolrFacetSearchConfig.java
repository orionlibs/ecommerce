package com.hybris.backoffice.solrsearch.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBackofficeIndexedTypeToSolrFacetSearchConfig extends GenericItem
{
    public static final String INDEXEDTYPE = "indexedType";
    public static final String SOLRFACETSEARCHCONFIG = "solrFacetSearchConfig";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("indexedType", Item.AttributeMode.INITIAL);
        tmp.put("solrFacetSearchConfig", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public ComposedType getIndexedType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "indexedType");
    }


    public ComposedType getIndexedType()
    {
        return getIndexedType(getSession().getSessionContext());
    }


    public void setIndexedType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "indexedType", value);
    }


    public void setIndexedType(ComposedType value)
    {
        setIndexedType(getSession().getSessionContext(), value);
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
        setProperty(ctx, "solrFacetSearchConfig", value);
    }


    public void setSolrFacetSearchConfig(SolrFacetSearchConfig value)
    {
        setSolrFacetSearchConfig(getSession().getSessionContext(), value);
    }
}
