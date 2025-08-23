package de.hybris.platform.warehousing.atp.dao.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.warehousing.atp.dao.AvailableToPromiseDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DefaultAvailableToPromiseDao extends AbstractItemDao implements AvailableToPromiseDao
{
    protected static final String STOCK_LEVELS = "stockLevels";
    protected static final String STOCK_LEVELS_RETURNED = "stockLevelsReturned";
    protected static final String STOCK_LEVELS_EXTERNAL = "stockLevelsExternal";


    public Long getAvailabilityForStockLevels(Map<String, Object> params)
    {
        if(((List)params.get("stockLevels")).isEmpty())
        {
            return Long.valueOf(0L);
        }
        String stockLevelQryString = "SELECT SUM({available}) FROM {StockLevel} WHERE {pk} IN (?stockLevels)";
        return returnAggregateQuantity("SELECT SUM({available}) FROM {StockLevel} WHERE {pk} IN (?stockLevels)", params, "stockLevels");
    }


    public Long getAllocationQuantityForStockLevels(Map<String, Object> params)
    {
        if(((List)params.get("stockLevels")).isEmpty())
        {
            return Long.valueOf(0L);
        }
        String inventoryEvtQryString = "SELECT SUM({quantity}) FROM {AllocationEvent as ae} WHERE {ae.stockLevel} IN (?stockLevels)";
        return returnAggregateQuantity("SELECT SUM({quantity}) FROM {AllocationEvent as ae} WHERE {ae.stockLevel} IN (?stockLevels)", params, "stockLevels");
    }


    public Long getCancellationQuantityForStockLevels(Map<String, Object> params)
    {
        if(((List)params.get("stockLevels")).isEmpty())
        {
            return Long.valueOf(0L);
        }
        String inventoryEvtQryString = "SELECT SUM({quantity}) FROM {CancellationEvent as ae} WHERE {ae.stockLevel} IN (?stockLevels)";
        return returnAggregateQuantity("SELECT SUM({quantity}) FROM {CancellationEvent as ae} WHERE {ae.stockLevel} IN (?stockLevels)", params, "stockLevels");
    }


    public Long getReservedQuantityForStockLevels(Map<String, Object> params)
    {
        if(((List)params.get("stockLevels")).isEmpty())
        {
            return Long.valueOf(0L);
        }
        String stockLevelQryString = "SELECT SUM({reserved}) FROM {StockLevel} WHERE {pk} IN (?stockLevels)";
        return returnAggregateQuantity("SELECT SUM({reserved}) FROM {StockLevel} WHERE {pk} IN (?stockLevels)", params, "stockLevels");
    }


    public Long getShrinkageQuantityForStockLevels(Map<String, Object> params)
    {
        if(((List)params.get("stockLevels")).isEmpty())
        {
            return Long.valueOf(0L);
        }
        String inventoryEvtQryString = "SELECT SUM({quantity}) FROM {ShrinkageEvent as ae} WHERE {ae.stockLevel} IN (?stockLevels)";
        return returnAggregateQuantity("SELECT SUM({quantity}) FROM {ShrinkageEvent as ae} WHERE {ae.stockLevel} IN (?stockLevels)", params, "stockLevels");
    }


    public Long getWastageQuantityForStockLevels(Map<String, Object> params)
    {
        if(((List)params.get("stockLevels")).isEmpty())
        {
            return Long.valueOf(0L);
        }
        String inventoryEvtQryString = "SELECT SUM({quantity}) FROM {WastageEvent as ae} WHERE {ae.stockLevel} IN (?stockLevels)";
        return returnAggregateQuantity("SELECT SUM({quantity}) FROM {WastageEvent as ae} WHERE {ae.stockLevel} IN (?stockLevels)", params, "stockLevels");
    }


    public Long getIncreaseQuantityForStockLevels(Map<String, Object> params)
    {
        if(((List)params.get("stockLevels")).isEmpty())
        {
            return Long.valueOf(0L);
        }
        String inventoryEvtQryString = "SELECT SUM({quantity}) FROM {IncreaseEvent as ae} WHERE {ae.stockLevel} IN (?stockLevels)";
        return returnAggregateQuantity("SELECT SUM({quantity}) FROM {IncreaseEvent as ae} WHERE {ae.stockLevel} IN (?stockLevels)", params, "stockLevels");
    }


    protected Long returnAggregateQuantity(String queryString, Map<String, Object> params, String key)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
        fQuery.addQueryParameter(key, params.get(key));
        List<Class<Long>> resultClassList = new ArrayList<>();
        resultClassList.add(Long.class);
        fQuery.setResultClassList(resultClassList);
        SearchResult<Long> result = getFlexibleSearchService().search(fQuery);
        return result.getResult().stream().filter(Objects::nonNull).findFirst().orElse(Long.valueOf(0L));
    }
}
