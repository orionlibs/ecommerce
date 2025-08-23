package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrClientsWrapper implements Closeable
{
    private static final Logger LOG = LoggerFactory.getLogger(SolrClientsWrapper.class);
    private final String configName;
    private final String configVersion;
    private CachedSolrClient searchClient;
    private CachedSolrClient indexClient;


    public SolrClientsWrapper(SolrConfig solrConfig)
    {
        this.configName = solrConfig.getName();
        this.configVersion = solrConfig.getVersion();
    }


    public String getConfigName()
    {
        return this.configName;
    }


    public String getConfigVersion()
    {
        return this.configVersion;
    }


    public CachedSolrClient getSearchClient()
    {
        return this.searchClient;
    }


    public void setSearchClient(CachedSolrClient searchClient)
    {
        this.searchClient = searchClient;
    }


    public CachedSolrClient getIndexClient()
    {
        return this.indexClient;
    }


    public void setIndexClient(CachedSolrClient indexClient)
    {
        this.indexClient = indexClient;
    }


    public void close()
    {
        LOG.info("Closing Solr clients [config={}]", getConfigName());
        IOUtils.closeQuietly((Closeable)this.searchClient);
        IOUtils.closeQuietly((Closeable)this.indexClient);
    }
}
