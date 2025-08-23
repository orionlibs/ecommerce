package com.hybris.backoffice.solrsearch.selenium.remote;

import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class TestBackofficeSolrTestingService implements BackofficeSolrTestingService
{
    private static final Logger LOG = LoggerFactory.getLogger(TestBackofficeSolrTestingService.class);
    protected BackofficeFacetSearchConfigService facetSearchConfigService;
    protected IndexerService indexerService;


    public void reindexSolr()
    {
        getFacetSearchConfigService().getAllMappedFacetSearchConfigs().forEach(config -> {
            try
            {
                getIndexerService().performFullIndex(config);
            }
            catch(IndexerException ex)
            {
                LOG.error(ex.getLocalizedMessage(), (Throwable)ex);
            }
        });
    }


    public IndexerService getIndexerService()
    {
        return this.indexerService;
    }


    @Required
    public void setIndexerService(IndexerService indexerService)
    {
        this.indexerService = indexerService;
    }


    public BackofficeFacetSearchConfigService getFacetSearchConfigService()
    {
        return this.facetSearchConfigService;
    }


    @Required
    public void setFacetSearchConfigService(BackofficeFacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }
}
