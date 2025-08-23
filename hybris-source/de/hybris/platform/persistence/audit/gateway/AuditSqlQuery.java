package de.hybris.platform.persistence.audit.gateway;

public class AuditSqlQuery
{
    private final String sqlQuery;
    private final Object[] queryPrams;
    private final int limitParamIndex;
    private final int offsetParamIndex;


    public AuditSqlQuery(String sqlQuery, Object[] queryPrams, int limitParamIndex, int offsetParamIndex)
    {
        this.sqlQuery = sqlQuery;
        this.queryPrams = queryPrams;
        this.limitParamIndex = limitParamIndex;
        this.offsetParamIndex = offsetParamIndex;
    }


    public void incrementOffset()
    {
        if(limitAndOffsetParamIndexSet())
        {
            this.queryPrams[this.offsetParamIndex] = Integer.valueOf(((Integer)this.queryPrams[this.offsetParamIndex]).intValue() + ((Integer)this.queryPrams[this.limitParamIndex]).intValue());
        }
    }


    public String getSqlQuery()
    {
        return this.sqlQuery;
    }


    public boolean isQueryWithLimit()
    {
        return (getLimit() > 0);
    }


    public int getLimit()
    {
        return limitAndOffsetParamIndexSet() ? ((Integer)this.queryPrams[this.limitParamIndex]).intValue() : -1;
    }


    public int getCurrentOffset()
    {
        return limitAndOffsetParamIndexSet() ? ((Integer)this.queryPrams[this.offsetParamIndex]).intValue() : -1;
    }


    public Object[] getQueryPrams()
    {
        return this.queryPrams;
    }


    private boolean limitAndOffsetParamIndexSet()
    {
        return (this.offsetParamIndex >= 0 && this.limitParamIndex >= 0);
    }
}
