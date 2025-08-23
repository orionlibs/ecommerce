package de.hybris.platform.personalizationservices.dao.impl;

import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.personalizationservices.dao.CxDaoQueryBuilder;
import de.hybris.platform.personalizationservices.dao.CxDaoStrategy;
import de.hybris.platform.personalizationservices.dao.CxDaoStrategySelector;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxDaoQueryBuilder implements CxDaoQueryBuilder
{
    private CxDaoStrategySelector cxDaoStrategySelector;


    public FlexibleSearchQuery buildQuery(String query, Map<String, Object> params)
    {
        FlexibleSearchQuery result = new FlexibleSearchQuery(query);
        if(params != null)
        {
            result.addQueryParameters(params);
        }
        return result;
    }


    public FlexibleSearchQuery buildQuery(String query, Map<String, Object> params, PaginationData pagination)
    {
        FlexibleSearchQuery result = buildQuery(query, params);
        int start = pagination.getCurrentPage() * pagination.getPageSize();
        int count = pagination.getPageSize();
        result.setStart(start);
        result.setCount(count);
        result.setNeedTotal(pagination.isNeedsTotal());
        return result;
    }


    public FlexibleSearchQuery buildQueryFromStrategy(FlexibleSearchQuery baseQuery, Collection<? extends CxDaoStrategy> strategies, Map<String, String> extraParams)
    {
        Optional<CxDaoStrategy> selectedStrategy = this.cxDaoStrategySelector.selectStrategy(strategies, extraParams);
        return selectedStrategy.<FlexibleSearchQuery>map(s -> getQuery(s, extraParams, baseQuery)).orElse(baseQuery);
    }


    protected FlexibleSearchQuery getQuery(CxDaoStrategy strategy, Map<String, String> extraParams, FlexibleSearchQuery source)
    {
        FlexibleSearchQuery target = strategy.getQuery(extraParams, source.getQueryParameters());
        if(MapUtils.isNotEmpty(source.getQueryParameters()))
        {
            target.addQueryParameters(source.getQueryParameters());
        }
        target.setNeedTotal(source.isNeedTotal());
        target.setStart(source.getStart());
        target.setCount(source.getCount());
        return target;
    }


    @Required
    public void setCxDaoStrategySelector(CxDaoStrategySelector cxDaoStrategySelector)
    {
        this.cxDaoStrategySelector = cxDaoStrategySelector;
    }


    protected CxDaoStrategySelector getCxDaoStrategySelector()
    {
        return this.cxDaoStrategySelector;
    }
}
