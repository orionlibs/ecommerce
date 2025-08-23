package de.hybris.platform.solrfacetsearch.jalo.config;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrEndpointUrl extends GenericItem
{
    public static final String URL = "url";
    public static final String MASTER = "master";
    public static final String SOLRSERVERCONFIG = "solrServerConfig";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrEndpointUrl> SOLRSERVERCONFIGHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRENDPOINTURL, false, "solrServerConfig", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("url", Item.AttributeMode.INITIAL);
        tmp.put("master", Item.AttributeMode.INITIAL);
        tmp.put("solrServerConfig", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SOLRSERVERCONFIGHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isMaster(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "master");
    }


    public Boolean isMaster()
    {
        return isMaster(getSession().getSessionContext());
    }


    public boolean isMasterAsPrimitive(SessionContext ctx)
    {
        Boolean value = isMaster(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isMasterAsPrimitive()
    {
        return isMasterAsPrimitive(getSession().getSessionContext());
    }


    public void setMaster(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "master", value);
    }


    public void setMaster(Boolean value)
    {
        setMaster(getSession().getSessionContext(), value);
    }


    public void setMaster(SessionContext ctx, boolean value)
    {
        setMaster(ctx, Boolean.valueOf(value));
    }


    public void setMaster(boolean value)
    {
        setMaster(getSession().getSessionContext(), value);
    }


    public SolrServerConfig getSolrServerConfig(SessionContext ctx)
    {
        return (SolrServerConfig)getProperty(ctx, "solrServerConfig");
    }


    public SolrServerConfig getSolrServerConfig()
    {
        return getSolrServerConfig(getSession().getSessionContext());
    }


    public void setSolrServerConfig(SessionContext ctx, SolrServerConfig value)
    {
        SOLRSERVERCONFIGHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSolrServerConfig(SolrServerConfig value)
    {
        setSolrServerConfig(getSession().getSessionContext(), value);
    }


    public String getUrl(SessionContext ctx)
    {
        return (String)getProperty(ctx, "url");
    }


    public String getUrl()
    {
        return getUrl(getSession().getSessionContext());
    }


    public void setUrl(SessionContext ctx, String value)
    {
        setProperty(ctx, "url", value);
    }


    public void setUrl(String value)
    {
        setUrl(getSession().getSessionContext(), value);
    }
}
