package de.hybris.platform.personalizationservices.dao.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.dao.CxDaoParamStrategy;
import de.hybris.platform.personalizationservices.dao.CxDaoQueryBuilder;
import de.hybris.platform.personalizationservices.dao.CxDaoStrategy;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractCxDao<M extends ItemModel> extends DefaultGenericDao<M>
{
    private CxDaoQueryBuilder cxDaoQueryBuilder;
    private PaginatedFlexibleSearchService paginatedFlexibleSearchService;


    public AbstractCxDao(String typecode)
    {
        super(typecode);
    }


    protected <T> Optional<T> querySingle(String query, Map<String, Object> params)
    {
        return querySingle(this.cxDaoQueryBuilder.buildQuery(query, params));
    }


    protected <T> Optional<T> querySingle(FlexibleSearchQuery query)
    {
        try
        {
            T result = (T)getFlexibleSearchService().searchUnique(query);
            return Optional.of(result);
        }
        catch(ModelNotFoundException e)
        {
            return Optional.empty();
        }
    }


    protected <T> List<T> queryList(String query)
    {
        return queryList(query, null);
    }


    protected <T> List<T> queryList(String query, Map<String, Object> params)
    {
        return queryList(this.cxDaoQueryBuilder.buildQuery(query, params));
    }


    protected <T> SearchPageData<T> queryList(String baseQuery, Map<String, Object> params, SearchPageData searchPageData)
    {
        FlexibleSearchQuery query = this.cxDaoQueryBuilder.buildQuery(baseQuery, params);
        PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
        parameter.setFlexibleSearchQuery(query);
        parameter.setSearchPageData(searchPageData);
        SearchPageData<T> searchResult = this.paginatedFlexibleSearchService.search(parameter);
        return searchResult;
    }


    protected <T> List<T> queryList(String baseQuery, Map<String, Object> params, Collection<? extends CxDaoStrategy> strategies, Map<String, String> extraParams)
    {
        FlexibleSearchQuery defaultQuery = this.cxDaoQueryBuilder.buildQuery(baseQuery, params);
        FlexibleSearchQuery targetQuery = this.cxDaoQueryBuilder.buildQueryFromStrategy(defaultQuery, strategies, extraParams);
        return queryList(targetQuery);
    }


    protected <T> SearchPageData<T> queryList(String baseQuery, Map<String, Object> params, Collection<? extends CxDaoStrategy> strategies, Map<String, String> extraParams, SearchPageData<?> searchPageData)
    {
        FlexibleSearchQuery defaultQuery = this.cxDaoQueryBuilder.buildQuery(baseQuery, params);
        FlexibleSearchQuery targetQuery = this.cxDaoQueryBuilder.buildQueryFromStrategy(defaultQuery, strategies, extraParams);
        PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
        parameter.setFlexibleSearchQuery(targetQuery);
        parameter.setSearchPageData(searchPageData);
        SearchPageData<T> searchResult = this.paginatedFlexibleSearchService.search(parameter);
        return searchResult;
    }


    protected <T> List<T> queryList(FlexibleSearchQuery query)
    {
        SearchResult<T> searchResult = getFlexibleSearchService().search(query);
        List<T> result = searchResult.getResult();
        return (result == null) ? Collections.<T>emptyList() : result;
    }


    protected Map<String, Object> expandParams(Map<String, Object> params, Map<String, String> externalParams, List<? extends CxDaoParamStrategy> strategies)
    {
        Map<String, Object> result = params;
        for(CxDaoParamStrategy strategy : strategies)
        {
            result = strategy.expandParams(result, externalParams);
        }
        return result;
    }


    protected <T> SearchPageData<T> buildEmptySearchPageData(PaginationData requestPagination)
    {
        SearchPageData<T> result = new SearchPageData();
        result.setPagination(requestPagination);
        result.setResults(Collections.emptyList());
        result.setSorts(Collections.emptyList());
        return result;
    }


    @Required
    public void setCxDaoQueryBuilder(CxDaoQueryBuilder cxDaoQueryBuilder)
    {
        this.cxDaoQueryBuilder = cxDaoQueryBuilder;
    }


    protected CxDaoQueryBuilder getCxDaoQueryBuilder()
    {
        return this.cxDaoQueryBuilder;
    }


    public void setPaginatedFlexibleSearchService(PaginatedFlexibleSearchService paginatedFlexibleSearchService)
    {
        this.paginatedFlexibleSearchService = paginatedFlexibleSearchService;
    }


    protected PaginatedFlexibleSearchService getPaginatedFlexibleSearchService()
    {
        return this.paginatedFlexibleSearchService;
    }
}
