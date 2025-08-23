package de.hybris.platform.solrfacetsearch.reporting;

import de.hybris.platform.solrfacetsearch.reporting.data.AggregatedSearchQueryInfo;
import java.util.List;

public interface SolrQueryStatisticsAggregator
{
    List<AggregatedSearchQueryInfo> aggregate();
}
