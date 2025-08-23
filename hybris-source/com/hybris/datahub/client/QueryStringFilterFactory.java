package com.hybris.datahub.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.hybris.datahub.dto.filter.CompositionFilterDto;
import com.hybris.datahub.dto.filter.DataItemFilterDto;
import com.hybris.datahub.dto.filter.DataLoadingFilterDto;
import com.hybris.datahub.dto.filter.PoolActionFilterDto;
import com.hybris.datahub.dto.filter.TargetSystemPublicationFilterDto;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

final class QueryStringFilterFactory
{
    static final String DATE_FORMAT = "yyyyMMddHHmmss";
    static final String POOL_NAME_PARAMETER = "poolname";
    static final String END_DATE_PARAMETER = "enddate";
    static final String START_DATE_PARAMETER = "startdate";
    static final String STATUS_PARAMETER = "status";
    static final String TARGET_SYSTEM_PARAMETER = "targetsystemname";


    static String createFromCompositionFilter(CompositionFilterDto filter)
    {
        return createPoolActionFilter((PoolActionFilterDto)filter);
    }


    private static String createPoolActionFilter(PoolActionFilterDto filter)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Preconditions.checkArgument((filter != null), "Query filter cannot be null");
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        if(filter.getPoolName() != null)
        {
            booleanQuery.add((Query)new TermQuery(new Term("poolname", filter.getPoolName())), BooleanClause.Occur.MUST);
        }
        if(filter.getEndDate() != null)
        {
            booleanQuery.add((Query)new TermQuery(new Term("enddate", filter.getEndDateOperator().getOperatorString() + filter.getEndDateOperator().getOperatorString())), BooleanClause.Occur.MUST);
        }
        if(filter.getStartDate() != null)
        {
            booleanQuery.add((Query)new TermQuery(new Term("startdate", filter.getStartDateOperator().getOperatorString() + filter.getStartDateOperator().getOperatorString())), BooleanClause.Occur.MUST);
        }
        if(filter.getStatuses() != null && (filter.getStatuses()).length > 0)
        {
            for(String status : filter.getStatuses())
            {
                booleanQuery.add((Query)new TermQuery(new Term("status", status)), BooleanClause.Occur.MUST);
            }
        }
        return booleanQuery.build().toString();
    }


    static String createFromStatusFilter(DataLoadingFilterDto filter)
    {
        Preconditions.checkArgument((filter != null), "Query filter cannot be null");
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        if(filter.getStatuses() != null && (filter.getStatuses()).length > 0)
        {
            for(String status : filter.getStatuses())
            {
                booleanQuery.add((Query)new TermQuery(new Term("status", status)), BooleanClause.Occur.MUST);
            }
        }
        return booleanQuery.build().toString();
    }


    @Deprecated(since = "6.1", forRemoval = true)
    static String createFromDataItemFilter(DataItemFilterDto filter)
    {
        return (new LuceneQueryStringFactory()).createFrom(filter);
    }


    static String createFromTargetSystemPublicationFilter(TargetSystemPublicationFilterDto filter)
    {
        StringBuilder searchString = new StringBuilder(createPoolActionFilter((PoolActionFilterDto)filter));
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        if(filter.getTargetSystemName() != null)
        {
            booleanQuery.add((Query)new TermQuery(new Term("targetsystemname", filter.getTargetSystemName())), BooleanClause.Occur.MUST);
            searchString.append(" ");
        }
        searchString.append(booleanQuery.build().toString());
        return searchString.toString();
    }


    public static String asUrlParameterValue(Object value)
    {
        try
        {
            String stringVal = (new ObjectMapper()).writeValueAsString(value);
            return URLEncoder.encode(stringVal, "UTF-8");
        }
        catch(IOException e)
        {
            throw new IllegalArgumentException("Failed to serialize " + value, e);
        }
    }
}
