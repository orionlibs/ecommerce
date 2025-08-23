package de.hybris.platform.util;

import de.hybris.platform.persistence.flexiblesearch.internal.QueryInterner;
import java.util.Collections;
import java.util.List;

public class SQLSearchResult<E> extends StandardSearchResult<E>
{
    private final String sql;
    private final List<Object> values;


    SQLSearchResult(List<E> list, int totalcount, int requestedstart, int requestedcount, String sql, List<Object> values, String dataSourceId)
    {
        super(list, totalcount, requestedstart, requestedcount, dataSourceId);
        this.sql = QueryInterner.intern(sql);
        this.values = Collections.unmodifiableList(values);
    }


    @Deprecated(since = "2105", forRemoval = true) SQLSearchResult(List<E> list, int totalcount, int requestedstart, int requestedcount, String sql, List<Object> values)
    {
        this(list, totalcount, requestedstart, requestedcount, sql, values, null);
    }


    public String getSQLForPreparedStatement()
    {
        return this.sql;
    }


    public List<Object> getValuesForPreparedStatement()
    {
        return this.values;
    }
}
