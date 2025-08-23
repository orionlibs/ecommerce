package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.cms2.services.SortQueryData;
import de.hybris.platform.cms2.services.SortQueryDataRegistry;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractCMSItemDao extends AbstractItemDao
{
    private SortQueryDataRegistry cmsSortQueryDataRegistry;


    protected String findSortQuery(Class<? extends Dao> typeClass, String sortCode)
    {
        Optional<SortQueryData> optional = Optional.<Optional<SortQueryData>>ofNullable(getCmsSortQueryDataRegistry().getSortQueryData(typeClass, sortCode)).orElse(Optional.<Optional<SortQueryData>>ofNullable(getCmsSortQueryDataRegistry().getDefaultSortQueryData(typeClass))
                        .orElse(Optional.empty()));
        return optional.<String>map(queryData -> queryData.getQuery()).orElse("");
    }


    protected FlexibleSearchQuery buildQuery(String baseQuery, Map<String, Object> params, int currentPage, int pageSize)
    {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(baseQuery);
        searchQuery.addQueryParameters(params);
        searchQuery.setStart(currentPage * pageSize);
        searchQuery.setCount(pageSize);
        searchQuery.setNeedTotal(true);
        return searchQuery;
    }


    protected SortQueryDataRegistry getCmsSortQueryDataRegistry()
    {
        return this.cmsSortQueryDataRegistry;
    }


    @Required
    public void setCmsSortQueryDataRegistry(SortQueryDataRegistry cmsSortQueryDataRegistry)
    {
        this.cmsSortQueryDataRegistry = cmsSortQueryDataRegistry;
    }
}
