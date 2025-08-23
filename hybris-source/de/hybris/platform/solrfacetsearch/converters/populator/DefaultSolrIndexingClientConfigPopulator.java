package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.common.ConfigurationUtils;
import de.hybris.platform.solrfacetsearch.config.SolrClientConfig;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;

public class DefaultSolrIndexingClientConfigPopulator implements Populator<SolrServerConfigModel, SolrClientConfig>
{
    public void populate(SolrServerConfigModel source, SolrClientConfig target)
    {
        target.setAliveCheckInterval(ConfigurationUtils.getInteger((ItemModel)source, "indexingAliveCheckInterval", "solr.config.%s.indexingclient.aliveCheckInterval", new Object[] {source
                        .getName()}));
        target.setConnectionTimeout(ConfigurationUtils.getInteger((ItemModel)source, "indexingConnectionTimeout", "solr.config.%s.indexingclient.connectionTimeout", new Object[] {source
                        .getName()}));
        target.setSocketTimeout(ConfigurationUtils.getInteger((ItemModel)source, "indexingSocketTimeout", "solr.config.%s.indexingclient.socketTimeout", new Object[] {source
                        .getName()}));
        target.setMaxConnections(ConfigurationUtils.getInteger((ItemModel)source, "indexingMaxTotalConnections", "solr.config.%s.indexingclient.maxConnections", new Object[] {source
                        .getName()}));
        target.setMaxConnectionsPerHost(
                        ConfigurationUtils.getInteger((ItemModel)source, "indexingMaxTotalConnectionsPerHostConfig", "solr.config.%s.indexingclient.maxConnectionsPerHost", new Object[] {source.getName()}));
        target.setTcpNoDelay(source.isTcpNoDelay());
    }
}
