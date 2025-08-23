package de.hybris.platform.solrfacetsearch.reporting;

import de.hybris.platform.solrfacetsearch.reporting.data.SearchQueryInfo;

public interface SolrReportingService
{
    void saveQueryResult(SearchQueryInfo paramSearchQueryInfo);
}
