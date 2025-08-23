package de.hybris.platform.util;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.LazyLoadMultiColumnList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.internal.CacheableResultHolder;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SQLSearchResultFactory
{
    public static <T> SearchResult<T> wrap(SearchResult<T> toWrap)
    {
        SQLSearchResult sQLSearchResult;
        SearchResult<T> result = toWrap;
        if(toWrap instanceof CacheableSearchResult)
        {
            CacheableSearchResult fromCache = (CacheableSearchResult)toWrap;
            List<T> list = createLazyLoadingResultList(fromCache.getResultsHolder());
            SQLSearchResult newResult = new SQLSearchResult(list, fromCache.getTotalCount(), fromCache.getRequestedStart(), fromCache.getRequestedCount(), fromCache.getSQLForPreparedStatement(), fromCache.getValuesForPreparedStatement(), fromCache.getDataSourceId());
            newResult.setFromCache(fromCache.isFromCache());
            sQLSearchResult = newResult;
        }
        return (SearchResult<T>)sQLSearchResult;
    }


    public static <T> SearchResult<T> createNonExecutable(int requestedstart, int requestedcount, String sql, List<Object> values, String dataSourceId)
    {
        return (SearchResult<T>)new NonExecutableSQLSearchResult(requestedstart, requestedcount, sql, values, dataSourceId);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public static <T> SearchResult<T> createNonExecutable(int requestedstart, int requestedcount, String sql, List<Object> values)
    {
        return (SearchResult<T>)new NonExecutableSQLSearchResult(requestedstart, requestedcount, sql, values);
    }


    public static <T> SearchResult<T> createCountOnlyResult(int totalCount, int requestedstart, int requestedcount, String sql, List<Object> values, String dataSourceId)
    {
        return (SearchResult<T>)new SQLSearchResult(Collections.emptyList(), totalCount, requestedstart, requestedcount, sql, values, dataSourceId);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public static <T> SearchResult<T> createCountOnlyResult(int totalCount, int requestedstart, int requestedcount, String sql, List<Object> values)
    {
        return (SearchResult<T>)new SQLSearchResult(Collections.EMPTY_LIST, totalCount, requestedstart, requestedcount, sql, values);
    }


    public static <T> SearchResult<T> createCacheable(CacheableResultHolder holder, int totalCount, int requestedstart, int requestedcount, String sql, List<Object> values, String dataSourceId)
    {
        return (SearchResult<T>)new CacheableSearchResult(holder, totalCount, requestedstart, requestedcount, sql, values, dataSourceId);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public static <T> SearchResult<T> createCacheable(CacheableResultHolder holder, int totalCount, int requestedstart, int requestedcount, String sql, List<Object> values)
    {
        return (SearchResult<T>)new CacheableSearchResult(holder, totalCount, requestedstart, requestedcount, sql, values);
    }


    protected static List createLazyLoadingResultList(CacheableResultHolder cachedResultsHolder)
    {
        List result;
        CacheableResultHolder.ConversionMetaInformation conversionResult = cachedResultsHolder.getConversionResult();
        int prefetchSize = cachedResultsHolder.getPrefetchSize();
        JDBCValueMappings.RowFetchResult rowFetchResult = cachedResultsHolder.getRowFetchResult();
        Set<PK> prefetchLanguages = cachedResultsHolder.getPrefetchLanguages();
        List<Class<?>> convertedResultClasses = conversionResult.getConvertedClasses();
        boolean mustWrapItems = conversionResult.isMustWrapItems();
        boolean mustWrapObjects = conversionResult.isMustWrapObjects();
        if(convertedResultClasses.size() == 1)
        {
            if(mustWrapItems && !mustWrapObjects)
            {
                LazyLoadItemList lazyLoadItemList = new LazyLoadItemList(prefetchLanguages, EJBTools.toPKList(rowFetchResult.rows), prefetchSize);
            }
            else if(mustWrapObjects)
            {
                List<Object> wrapped = new ArrayList(rowFetchResult.rows.size());
                Cache cache = Registry.getCurrentTenantNoFallback().getCache();
                for(Object o : rowFetchResult.rows)
                {
                    wrapped.add((o != null) ? WrapperFactory.wrap(cache, o) : null);
                }
                result = wrapped;
            }
            else
            {
                result = rowFetchResult.rows;
            }
        }
        else if(mustWrapItems || mustWrapObjects)
        {
            LazyLoadMultiColumnList lazyLoadMultiColumnList = new LazyLoadMultiColumnList(rowFetchResult.rows, convertedResultClasses, prefetchLanguages, prefetchSize, mustWrapObjects);
        }
        else
        {
            result = rowFetchResult.rows;
        }
        return result;
    }
}
