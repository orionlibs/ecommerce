package de.hybris.platform.solrserver.strategies;

import java.util.Map;

public interface SolrServerConfigurationProvider
{
    Map<String, String> getConfiguration();
}
