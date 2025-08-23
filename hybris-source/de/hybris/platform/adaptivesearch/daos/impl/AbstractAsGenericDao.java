package de.hybris.platform.adaptivesearch.daos.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractAsGenericDao<T extends ItemModel> extends DefaultGenericDao<T>
{
    public AbstractAsGenericDao(String typecode)
    {
        super(typecode);
    }


    protected StringBuilder createQuery(String typeCode)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} FROM {").append(typeCode).append('}');
        return builder;
    }


    protected void appendWhereClause(StringBuilder query)
    {
        query.append(" WHERE");
    }


    protected void appendAndClause(StringBuilder query)
    {
        query.append(" AND");
    }


    protected void appendClause(StringBuilder query, Map<String, Object> parameters, String paramName, Object paramValue)
    {
        if(paramValue == null)
        {
            query.append('{').append(paramName).append("} IS NULL");
        }
        else if(paramValue instanceof java.util.Collection)
        {
            query.append(" {").append(paramName).append("} in (?").append(paramName).append(")");
            parameters.put(paramName, paramValue);
        }
        else
        {
            query.append(" {").append(paramName).append("}=?").append(paramName);
            parameters.put(paramName, paramValue);
        }
    }


    protected void appendLikeClause(StringBuilder query, Map<String, Object> parameters, String paramName, String paramValue)
    {
        if(paramValue == null)
        {
            query.append('{').append(paramName).append("} IS NULL");
        }
        else
        {
            query.append(" LOWER({").append(paramName).append("}) LIKE LOWER(?").append(paramName).append(")");
            parameters.put(paramName, paramValue);
        }
    }


    protected void appendOrderByClause(StringBuilder query, String paramName, boolean asc)
    {
        query.append(" ORDER BY{").append(paramName).append("} ").append(asc ? "ASC" : "DESC");
    }


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


    protected <T> List<T> queryList(String baseQuery, Map<String, Object> params)
    {
        FlexibleSearchQuery fsQuery = buildQuery(baseQuery, params);
        SearchResult<T> searchResult = getFlexibleSearchService().search(fsQuery);
        List<T> result = searchResult.getResult();
        return (result == null) ? Collections.<T>emptyList() : result;
    }


    protected <T> SearchPageData<T> queryList(String baseQuery, Map<String, Object> params, PaginationData pagination)
    {
        FlexibleSearchQuery fsQuery = buildQuery(baseQuery, params, pagination);
        SearchResult<T> searchResult = getFlexibleSearchService().search(fsQuery);
        return buildSearchPageData(searchResult, pagination);
    }


    protected <T> SearchPageData<T> buildSearchPageData(SearchResult<T> searchResult, PaginationData requestPagination)
    {
        SearchPageData<T> result = new SearchPageData();
        PaginationData pagination = buildPagination(searchResult);
        pagination.setNeedsTotal(requestPagination.isNeedsTotal());
        if(!requestPagination.isNeedsTotal())
        {
            pagination.setNumberOfPages(0);
            pagination.setTotalNumberOfResults(0L);
        }
        result.setResults(searchResult.getResult());
        result.setPagination(pagination);
        result.setSorts(Collections.emptyList());
        return result;
    }


    protected PaginationData buildPagination(SearchResult<?> search)
    {
        PaginationData result = new PaginationData();
        result.setTotalNumberOfResults(search.getTotalCount());
        result.setPageSize(search.getCount());
        int reqCount = search.getRequestedCount();
        if(reqCount > 0)
        {
            result.setCurrentPage(search.getRequestedStart() / search.getRequestedCount());
        }
        else
        {
            result.setCurrentPage(0);
        }
        double totalPages = Math.ceil(1.0D * search.getTotalCount() / search.getRequestedCount());
        if(Double.isFinite(totalPages))
        {
            result.setNumberOfPages((int)totalPages);
        }
        else
        {
            result.setNumberOfPages(0);
        }
        return result;
    }
}
