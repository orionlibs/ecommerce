package de.hybris.platform.solrfacetsearch.reporting;

import de.hybris.platform.solrfacetsearch.reporting.data.AggregatedSearchQueryInfo;
import java.util.List;

public interface AggregatedQueryStatsService
{
    void save(List<AggregatedSearchQueryInfo> paramList);
}
