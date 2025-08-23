package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.common.ConfigurationUtils;
import de.hybris.platform.solrfacetsearch.config.SolrClientConfig;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;

public class DefaultSolrClientConfigCredentialsPopulator implements Populator<SolrServerConfigModel, SolrClientConfig>
{
    public void populate(SolrServerConfigModel source, SolrClientConfig target)
    {
        target.setUsername(ConfigurationUtils.getString((ItemModel)source, "username", "solr.config.%s.client.username", new Object[] {source
                        .getName()}));
        target.setPassword(ConfigurationUtils.getString((ItemModel)source, "password", "solr.config.%s.client.password", new Object[] {source
                        .getName()}));
    }
}
