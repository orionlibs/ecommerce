package de.hybris.platform.servicelayer.search.paginated.util;

import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;

public final class PaginatedSearchUtils
{
    public static PaginationData createPaginationData(int pageSize, int currentPage, boolean needsTotal)
    {
        PaginationData paginationData = new PaginationData();
        paginationData.setPageSize(pageSize);
        paginationData.setCurrentPage(currentPage);
        paginationData.setNeedsTotal(needsTotal);
        return paginationData;
    }


    public static SortData createSortData(String code, boolean asc)
    {
        SortData sortData = new SortData();
        sortData.setCode(code);
        sortData.setAsc(asc);
        return sortData;
    }


    public static <T> SearchPageData<T> createSearchPageDataWithPagination(int pageSize, int currentPage, boolean needsTotal)
    {
        return createSearchPageDataWithPaginationAndSorting(pageSize, currentPage, needsTotal, Collections.EMPTY_MAP);
    }


    public static <T> SearchPageData<T> createSearchPageDataWithPaginationAndSorting(int pageSize, int currentPage, boolean needsTotal, Map<String, String> sortMap)
    {
        SearchPageData<T> searchPageData = new SearchPageData();
        PaginationData pagination = createPaginationData(pageSize, currentPage, needsTotal);
        searchPageData.setPagination(pagination);
        if(CollectionUtils.isEmpty(sortMap))
        {
            searchPageData.setSorts(Collections.emptyList());
            return searchPageData;
        }
        List<SortData> sortList = new ArrayList<>();
        for(Map.Entry<String, String> sort : sortMap.entrySet())
        {
            String sortCode = sort.getKey();
            boolean isAsc = !"desc".equalsIgnoreCase(sort.getValue());
            SortData sortData = createSortData(sortCode, isAsc);
            sortList.add(sortData);
        }
        searchPageData.setSorts(sortList);
        return searchPageData;
    }
}
