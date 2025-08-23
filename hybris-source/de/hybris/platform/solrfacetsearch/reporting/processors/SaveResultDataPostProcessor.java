package de.hybris.platform.solrfacetsearch.reporting.processors;

import de.hybris.platform.solrfacetsearch.reporting.SolrReportingService;
import de.hybris.platform.solrfacetsearch.reporting.data.SearchQueryInfo;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.SolrResultPostProcessor;

public class SaveResultDataPostProcessor implements SolrResultPostProcessor
{
    private SolrReportingService solrReportingService;
    private boolean enableCollectingStatistics;


    public SearchResult process(SearchResult solrSearchResult)
    {
        if(this.enableCollectingStatistics)
        {
            SearchQueryInfo result = solrSearchResult.getQueryInfo();
            this.solrReportingService.saveQueryResult(result);
        }
        return solrSearchResult;
    }


    public void setSolrReportingService(SolrReportingService solrReportingService)
    {
        this.solrReportingService = solrReportingService;
    }


    public void setEnableCollectingStatistics(boolean enableCollectingStatistics)
    {
        this.enableCollectingStatistics = enableCollectingStatistics;
    }
}
