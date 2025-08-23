package de.hybris.platform.servicelayer.internal.dao;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class GenericDaoSearchQueryBuilder
{
    private final String typeCode;
    private Map<String, ? extends Object> params = Collections.emptyMap();
    private SortParameters sortParams = new SortParameters();
    private int count = -1;


    GenericDaoSearchQueryBuilder(String typeCode)
    {
        this.typeCode = Objects.<String>requireNonNull(typeCode, "typeCode mustn't be null.");
    }


    GenericDaoSearchQueryBuilder withParams(Map<String, ? extends Object> params)
    {
        this.params = (params == null) ? Collections.<String, Object>emptyMap() : params;
        return this;
    }


    GenericDaoSearchQueryBuilder withSortParams(SortParameters sortParams)
    {
        this.sortParams = (sortParams == null) ? new SortParameters() : sortParams;
        return this;
    }


    GenericDaoSearchQueryBuilder withCount(int count)
    {
        this.count = count;
        return this;
    }


    FlexibleSearchQuery build()
    {
        StringBuilder builder = createQueryString();
        appendWhereClausesToBuilder(builder);
        appendOrderByClausesToBuilder(builder);
        FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
        if(this.count == -1)
        {
            query.setNeedTotal(true);
        }
        else
        {
            query.setCount(this.count);
        }
        if(!this.params.isEmpty())
        {
            query.addQueryParameters(this.params);
        }
        return query;
    }


    private StringBuilder createQueryString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GET {").append(this.typeCode).append("}");
        return builder;
    }


    private void appendWhereClausesToBuilder(StringBuilder builder)
    {
        if(this.params.isEmpty())
        {
            return;
        }
        builder.append(" WHERE ")
                        .append(this.params
                                        .keySet().stream().map(k -> String.format("{%s}=?%s", new Object[] {k, k})).collect(Collectors.joining(" AND ")));
    }


    private void appendOrderByClausesToBuilder(StringBuilder builder)
    {
        if(this.sortParams.isEmpty())
        {
            return;
        }
        builder.append(" ORDER BY ").append(this.sortParams.getSortParameters()
                        .entrySet()
                        .stream()
                        .map(e -> String.format("{%s} %s", new Object[] {e.getKey(), e.getValue()})).collect(Collectors.joining(", ")));
    }
}
