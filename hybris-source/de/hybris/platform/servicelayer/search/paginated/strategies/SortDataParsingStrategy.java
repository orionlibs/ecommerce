package de.hybris.platform.servicelayer.search.paginated.strategies;

import de.hybris.platform.core.servicelayer.data.SortData;
import java.util.Map;

public interface SortDataParsingStrategy
{
    String getQueryAlias(Map<String, String> paramMap, SortData paramSortData);


    String getSortCode(Map<String, String> paramMap, SortData paramSortData);


    String getSortDirection(Map<String, String> paramMap, SortData paramSortData);
}
