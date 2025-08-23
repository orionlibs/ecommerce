package de.hybris.platform.solrfacetsearch.reporting;

import de.hybris.platform.solrfacetsearch.reporting.data.AggregatedSearchQueryInfo;
import de.hybris.platform.solrfacetsearch.reporting.data.SearchQueryInfo;
import java.util.List;

public interface StatisticsCollector
{
    void addStatistic(SearchQueryInfo paramSearchQueryInfo);


    List<AggregatedSearchQueryInfo> getAggregatedStatistics();


    void clear();
}
