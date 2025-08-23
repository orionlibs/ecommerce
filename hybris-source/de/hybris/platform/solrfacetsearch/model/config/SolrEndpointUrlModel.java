package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrEndpointUrlModel extends ItemModel
{
    public static final String _TYPECODE = "SolrEndpointUrl";
    public static final String _SOLRSERVERCONFIG2SOLRENDPOINTURL = "SolrServerConfig2SolrEndpointUrl";
    public static final String URL = "url";
    public static final String MASTER = "master";
    public static final String SOLRSERVERCONFIG = "solrServerConfig";


    public SolrEndpointUrlModel()
    {
    }


    public SolrEndpointUrlModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrEndpointUrlModel(String _url)
    {
        setUrl(_url);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrEndpointUrlModel(ItemModel _owner, String _url)
    {
        setOwner(_owner);
        setUrl(_url);
    }


    @Accessor(qualifier = "solrServerConfig", type = Accessor.Type.GETTER)
    public SolrServerConfigModel getSolrServerConfig()
    {
        return (SolrServerConfigModel)getPersistenceContext().getPropertyValue("solrServerConfig");
    }


    @Accessor(qualifier = "url", type = Accessor.Type.GETTER)
    public String getUrl()
    {
        return (String)getPersistenceContext().getPropertyValue("url");
    }


    @Accessor(qualifier = "master", type = Accessor.Type.GETTER)
    public boolean isMaster()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("master"));
    }


    @Accessor(qualifier = "master", type = Accessor.Type.SETTER)
    public void setMaster(boolean value)
    {
        getPersistenceContext().setPropertyValue("master", toObject(value));
    }


    @Accessor(qualifier = "solrServerConfig", type = Accessor.Type.SETTER)
    public void setSolrServerConfig(SolrServerConfigModel value)
    {
        getPersistenceContext().setPropertyValue("solrServerConfig", value);
    }


    @Accessor(qualifier = "url", type = Accessor.Type.SETTER)
    public void setUrl(String value)
    {
        getPersistenceContext().setPropertyValue("url", value);
    }
}
