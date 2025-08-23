package de.hybris.platform.servicelayer.search.paginated.dao;

import de.hybris.platform.core.servicelayer.data.SearchPageData;
import java.util.Map;

public interface PaginatedGenericDao<M>
{
    SearchPageData<M> find(SearchPageData<M> paramSearchPageData);


    SearchPageData<M> find(Map<String, ?> paramMap, SearchPageData paramSearchPageData);
}
