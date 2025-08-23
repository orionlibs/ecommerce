package de.hybris.platform.servicelayer.search.paginated.strategies.impl;

import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.servicelayer.search.paginated.strategies.SortDataParsingStrategy;
import java.util.Map;

public class DefaultSortDataParsingStrategy implements SortDataParsingStrategy
{
    public String getQueryAlias(Map<String, String> sortCodeToQueryAlias, SortData sortData)
    {
        return sortCodeToQueryAlias.get(sortData.getCode().toLowerCase());
    }


    public String getSortCode(Map<String, String> sortCodeToQueryAlias, SortData sortData)
    {
        return sortCodeToQueryAlias.containsKey(sortData.getCode().toLowerCase()) ? sortData.getCode().toLowerCase() : "";
    }


    public String getSortDirection(Map<String, String> sortCodeToQueryAlias, SortData sortData)
    {
        return sortData.isAsc() ? "asc" : "desc";
    }
}
