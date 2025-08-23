package de.hybris.platform.servicelayer.internal.dao;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

public class DefaultGenericDao<M extends ItemModel> implements GenericDao<M>
{
    private final String typecode;
    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;


    public DefaultGenericDao(String typecode)
    {
        this.typecode = typecode;
    }


    public List<M> find()
    {
        FlexibleSearchQuery query = newQueryBuilder().build();
        return getFlexibleSearchService().search(query).getResult();
    }


    public List<M> find(Map<String, ? extends Object> params)
    {
        FlexibleSearchQuery query = newQueryBuilder().withParams(params).build();
        FlexibleSearchService fss = getFlexibleSearchService();
        SearchResult<M> searchResult = fss.search(query);
        return searchResult.getResult();
    }


    public List<M> find(SortParameters sortParams)
    {
        FlexibleSearchQuery query = newQueryBuilder().withSortParams(sortParams).build();
        return getFlexibleSearchService().search(query).getResult();
    }


    public List<M> find(Map<String, ? extends Object> params, SortParameters sortParams)
    {
        FlexibleSearchQuery query = newQueryBuilder().withParams(params).withSortParams(sortParams).build();
        return getFlexibleSearchService().search(query).getResult();
    }


    public List<M> find(Map<String, ? extends Object> params, SortParameters sortParams, int count)
    {
        FlexibleSearchQuery query = newQueryBuilder().withParams(params).withSortParams(sortParams).withCount(count).build();
        return getFlexibleSearchService().search(query).getResult();
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    private GenericDaoSearchQueryBuilder newQueryBuilder()
    {
        return new GenericDaoSearchQueryBuilder(this.typecode);
    }
}
