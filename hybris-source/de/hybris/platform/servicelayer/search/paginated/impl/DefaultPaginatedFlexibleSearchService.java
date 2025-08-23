package de.hybris.platform.servicelayer.search.paginated.impl;

import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.AbstractQueryHelper;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import de.hybris.platform.servicelayer.search.paginated.strategies.SortDataParsingStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

public class DefaultPaginatedFlexibleSearchService implements PaginatedFlexibleSearchService
{
    private FlexibleSearchService flexibleSearchService;
    private SortDataParsingStrategy sortDataParsingStrategy;
    private AbstractQueryHelper abstractQueryHelper;


    public <T> SearchPageData<T> search(PaginatedFlexibleSearchParameter parameter)
    {
        validatePaginatedFlexibleSearchParameter(parameter);
        boolean nextElementAdded = shouldAddNextElement(parameter);
        FlexibleSearchQuery searchQuery = parameter.getFlexibleSearchQuery();
        SearchPageData searchPageData = parameter.getSearchPageData();
        Map<String, String> sortCodeToQueryAlias = parameter.getSortCodeToQueryAlias();
        FlexibleSearchQuery populatedSearchQuery = populateSearchQuery(searchPageData, searchQuery, sortCodeToQueryAlias, nextElementAdded);
        SearchResult<T> searchResult = getFlexibleSearchService().search(populatedSearchQuery);
        SearchResultInfo searchResultInfo = new SearchResultInfo(this, searchResult, nextElementAdded);
        return createPagedSearchResult(searchResult, searchPageData, searchResultInfo);
    }


    private boolean shouldAddNextElement(PaginatedFlexibleSearchParameter parameter)
    {
        return (parameter.getSearchPageData().getPagination().getPageSize() > 0 &&
                        !parameter.getSearchPageData().getPagination().isNeedsTotal());
    }


    protected void validatePaginatedFlexibleSearchParameter(PaginatedFlexibleSearchParameter parameter)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("parameter", parameter);
        ServicesUtil.validateParameterNotNullStandardMessage("searchQuery", parameter.getFlexibleSearchQuery());
        SearchPageData searchPageDataInput = parameter.getSearchPageData();
        ServicesUtil.validateParameterNotNullStandardMessage("searchPageData", searchPageDataInput);
        ServicesUtil.validateParameterNotNullStandardMessage("paginationData", searchPageDataInput.getPagination());
        Assert.isTrue((searchPageDataInput.getPagination().getCurrentPage() >= 0), "paginationData current page must be zero or greater");
    }


    protected FlexibleSearchQuery populateSearchQuery(SearchPageData searchPageData, FlexibleSearchQuery searchQuery, Map<String, String> sortCodeToQueryAlias, boolean nextElementAdded)
    {
        List<SortData> filteredSortList = filterSearchPageDataSorts(searchPageData, sortCodeToQueryAlias);
        searchPageData.setSorts(filteredSortList);
        String sortsAppliedQuery = buildSortsAppliedQuery(searchQuery.getQuery(), searchPageData, sortCodeToQueryAlias);
        FlexibleSearchQuery updatedSearchQuery = getAbstractQueryHelper().getUpdatedFlexibleSearchQuery(searchQuery, sortsAppliedQuery);
        PaginationData pagination = searchPageData.getPagination();
        updatedSearchQuery.setNeedTotal(pagination.isNeedsTotal());
        updatedSearchQuery.setStart(pagination.getCurrentPage() * pagination.getPageSize());
        if(nextElementAdded)
        {
            updatedSearchQuery.setCount(pagination.getPageSize() + 1);
        }
        else
        {
            updatedSearchQuery.setCount(pagination.getPageSize());
        }
        return updatedSearchQuery;
    }


    protected List<SortData> filterSearchPageDataSorts(SearchPageData searchPageData, Map<String, String> sortCodeToQueryAlias)
    {
        List<SortData> requestedSortList = searchPageData.getSorts();
        List<SortData> filteredSortList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(requestedSortList))
        {
            for(SortData sort : requestedSortList)
            {
                if(sort.getCode().equalsIgnoreCase(getSortDataParsingStrategy().getSortCode(sortCodeToQueryAlias, sort)))
                {
                    sort.setCode(sort.getCode().toLowerCase());
                    filteredSortList.add(sort);
                }
            }
        }
        return filteredSortList;
    }


    protected String buildSortsAppliedQuery(String query, SearchPageData searchPageData, Map<String, String> sortCodeToQueryAlias)
    {
        StringBuilder queryBuilder = new StringBuilder(query);
        List<SortData> sorts = searchPageData.getSorts();
        if(CollectionUtils.isNotEmpty(sorts))
        {
            queryBuilder.append(" ").append("ORDER BY ");
            boolean firstParam = true;
            for(SortData sort : sorts)
            {
                if(!firstParam)
                {
                    queryBuilder.append(",").append(" ");
                }
                queryBuilder.append("{")
                                .append(getSortDataParsingStrategy().getQueryAlias(sortCodeToQueryAlias, sort))
                                .append(":")
                                .append(getSortDataParsingStrategy().getSortCode(sortCodeToQueryAlias, sort))
                                .append("}")
                                .append(" ")
                                .append(sort.isAsc() ? "asc" : "desc");
                firstParam = false;
            }
        }
        return queryBuilder.toString();
    }


    protected <T> SearchPageData<T> createPagedSearchResult(SearchResult<T> searchResult, SearchPageData searchPageData, SearchResultInfo searchResultInfo)
    {
        SearchPageData<T> result = createSearchPageData();
        if(searchResultInfo.nextElementAdded)
        {
            result.setResults(searchResult.getResult().subList(0, searchResultInfo.count));
        }
        else
        {
            result.setResults(searchResult.getResult());
        }
        result.setPagination(createPagination(searchPageData, searchResultInfo));
        result.setSorts(searchPageData.getSorts());
        return result;
    }


    protected <T> PaginationData createPagination(SearchPageData searchPageDataInput, SearchResultInfo searchResultInfo)
    {
        PaginationData paginationDataInput = searchPageDataInput.getPagination();
        PaginationData paginationDataOutput = new PaginationData();
        paginationDataOutput.setPageSize(paginationDataInput.getPageSize());
        paginationDataOutput.setTotalNumberOfResults(searchResultInfo.totalCount);
        paginationDataOutput.setNeedsTotal(paginationDataInput.isNeedsTotal());
        paginationDataOutput.setHasPrevious(searchResultInfo.hasPrevious);
        paginationDataOutput.setHasNext(searchResultInfo.hasNext);
        int reqCount = searchResultInfo.requestedCount;
        if(reqCount > 0)
        {
            paginationDataOutput.setCurrentPage(searchResultInfo.requestedStart / searchResultInfo.requestedCount);
        }
        else
        {
            paginationDataOutput.setCurrentPage(0);
        }
        double totalPages = Math.ceil(1.0D * searchResultInfo.totalCount / searchResultInfo.requestedCount);
        if(Double.isFinite(totalPages))
        {
            paginationDataOutput.setNumberOfPages((int)totalPages);
        }
        else
        {
            paginationDataOutput.setNumberOfPages(0);
        }
        return paginationDataOutput;
    }


    protected <T> SearchPageData<T> createSearchPageData()
    {
        return new SearchPageData();
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected SortDataParsingStrategy getSortDataParsingStrategy()
    {
        return this.sortDataParsingStrategy;
    }


    @Required
    public void setSortDataParsingStrategy(SortDataParsingStrategy sortDataParsingStrategy)
    {
        this.sortDataParsingStrategy = sortDataParsingStrategy;
    }


    protected AbstractQueryHelper getAbstractQueryHelper()
    {
        return this.abstractQueryHelper;
    }


    @Required
    public void setAbstractQueryHelper(AbstractQueryHelper abstractQueryHelper)
    {
        this.abstractQueryHelper = abstractQueryHelper;
    }
}
