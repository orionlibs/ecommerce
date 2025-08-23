package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.EndpointURL;
import de.hybris.platform.solrfacetsearch.model.config.SolrEndpointUrlModel;

public class DefaultEndpointUrlPopulator implements Populator<SolrEndpointUrlModel, EndpointURL>
{
    public void populate(SolrEndpointUrlModel source, EndpointURL target)
    {
        target.setUrl(source.getUrl());
        target.setMaster(source.isMaster());
        target.setModifiedTime(source.getModifiedtime());
    }
}
