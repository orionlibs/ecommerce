package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.common.ConfigurationUtils;
import de.hybris.platform.solrfacetsearch.config.SolrClientConfig;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;

public class DefaultSolrClientConfigPopulator implements Populator<SolrServerConfigModel, SolrClientConfig>
{
    public void populate(SolrServerConfigModel source, SolrClientConfig target)
    {
        target.setAliveCheckInterval(ConfigurationUtils.getInteger((ItemModel)source, "aliveCheckInterval", "solr.config.%s.client.aliveCheckInterval", new Object[] {source
                        .getName()}));
        target.setConnectionTimeout(ConfigurationUtils.getInteger((ItemModel)source, "connectionTimeout", "solr.config.%s.client.connectionTimeout", new Object[] {source
                        .getName()}));
        target.setSocketTimeout(ConfigurationUtils.getInteger((ItemModel)source, "socketTimeout", "solr.config.%s.client.socketTimeout", new Object[] {source
                        .getName()}));
        target.setMaxConnections(ConfigurationUtils.getInteger((ItemModel)source, "maxTotalConnections", "solr.config.%s.client.maxConnections", new Object[] {source
                        .getName()}));
        target.setMaxConnectionsPerHost(
                        ConfigurationUtils.getInteger((ItemModel)source, "maxTotalConnectionsPerHostConfig", "solr.config.%s.client.maxConnectionsPerHost", new Object[] {source.getName()}));
        target.setTcpNoDelay(source.isTcpNoDelay());
    }
}
