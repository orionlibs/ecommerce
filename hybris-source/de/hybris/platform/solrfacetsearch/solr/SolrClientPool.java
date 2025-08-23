package de.hybris.platform.solrfacetsearch.solr;

import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.solrfacetsearch.solr.impl.CachedSolrClient;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.solr.client.solrj.SolrClient;

public interface SolrClientPool
{
    CachedSolrClient getOrCreate(Index paramIndex, SolrClientType paramSolrClientType, Function<SolrConfig, SolrClient> paramFunction) throws SolrServiceException;


    CachedSolrClient getOrCreate(Index paramIndex, SolrClientType paramSolrClientType, Function<SolrConfig, SolrClient> paramFunction, Consumer<SolrClient> paramConsumer) throws SolrServiceException;


    void invalidateAll();
}
