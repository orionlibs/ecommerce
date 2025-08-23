package de.hybris.platform.personalizationwebservices.queries.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractFlexibleSearchRestQueryExecutor<T, Q> extends AbstractRestQueryExecutor
{
    private FlexibleSearchService flexibleSearchService;


    public Object executeAfterValidation(Map<String, String> params)
    {
        String query = createQuery(params);
        Map<String, Object> queryParams = createQueryParmas(params);
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query, queryParams);
        fQuery.setResultClassList(getResultClasses());
        SearchResult<Q> searchResult = this.flexibleSearchService.search(fQuery);
        return readResults(searchResult);
    }


    protected abstract String createQuery(Map<String, String> paramMap);


    protected abstract Map<String, Object> createQueryParmas(Map<String, String> paramMap);


    protected abstract T readResults(SearchResult<Q> paramSearchResult);


    protected List<Class<?>> getResultClasses()
    {
        return Collections.singletonList(ItemModel.class);
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }
}
