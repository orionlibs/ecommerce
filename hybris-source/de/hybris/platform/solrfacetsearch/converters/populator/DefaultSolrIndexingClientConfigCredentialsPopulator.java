package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.common.ConfigurationUtils;
import de.hybris.platform.solrfacetsearch.config.SolrClientConfig;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;

public class DefaultSolrIndexingClientConfigCredentialsPopulator implements Populator<SolrServerConfigModel, SolrClientConfig>
{
    public void populate(SolrServerConfigModel source, SolrClientConfig target)
    {
        target.setUsername(ConfigurationUtils.getString((ItemModel)source, "indexingUsername", "solr.config.%s.indexingclient.username", new Object[] {source
                        .getName()}));
        target.setPassword(ConfigurationUtils.getString((ItemModel)source, "indexingPassword", "solr.config.%s.indexingclient.password", new Object[] {source
                        .getName()}));
    }
}
