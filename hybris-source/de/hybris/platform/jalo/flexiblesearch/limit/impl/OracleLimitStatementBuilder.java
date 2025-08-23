package de.hybris.platform.jalo.flexiblesearch.limit.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.jalo.flexiblesearch.limit.LimitStatementBuilder;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery;
import java.util.ArrayList;
import java.util.List;

public class OracleLimitStatementBuilder implements LimitStatementBuilder
{
    private final List<Object> modifiedStatementValues;
    private final String modifiedStatement;
    private final int originalStart;
    private final int originalCount;


    public OracleLimitStatementBuilder(TranslatedQuery.ExecutableQuery originalQuery, int originalStart, int originalCount)
    {
        Preconditions.checkArgument((originalStart >= 0), "start parameter cannot be lower than 0");
        this.originalStart = originalStart;
        this.originalCount = originalCount;
        this.modifiedStatement = buildModifiedQuery(originalQuery);
        this.modifiedStatementValues = buildStatementValues(originalQuery.getValueList());
    }


    private List<Object> buildStatementValues(List<Object> valueList)
    {
        List<Object> result;
        int maxRownum = (this.originalCount < 0) ? Integer.MAX_VALUE : (this.originalStart + this.originalCount);
        if(hasOffset())
        {
            result = new ArrayList(valueList.size() + 2);
            result.addAll(valueList);
            result.add(Integer.valueOf(maxRownum));
            result.add(Integer.valueOf(this.originalStart));
        }
        else
        {
            result = new ArrayList(valueList.size() + 1);
            result.addAll(valueList);
            result.add(Integer.valueOf(maxRownum));
        }
        return result;
    }


    private String buildModifiedQuery(TranslatedQuery.ExecutableQuery originalQuery)
    {
        String originalSQL = originalQuery.getSQL();
        Preconditions.checkArgument((originalSQL != null), "ExecutableQuery objects does not contains SQL query!");
        StringBuilder pagingQuery = new StringBuilder(originalSQL.length() + 100);
        boolean hasOffset = hasOffset();
        if(hasOffset)
        {
            pagingQuery.append("SELECT * FROM (SELECT row_.*, rownum rownum_ FROM (");
        }
        else
        {
            pagingQuery.append("SELECT * FROM (");
        }
        pagingQuery.append(originalSQL);
        if(hasOffset)
        {
            pagingQuery.append(") row_ WHERE rownum <= ?) WHERE rownum_ > ?");
        }
        else
        {
            pagingQuery.append(") WHERE rownum <= ?");
        }
        return pagingQuery.toString();
    }


    private boolean hasOffset()
    {
        return (this.originalStart > 0);
    }


    public boolean hasDbEngineLimitSupport()
    {
        return true;
    }


    public List<Object> getModifiedStatementValues()
    {
        return this.modifiedStatementValues;
    }


    public String getModifiedStatement()
    {
        return this.modifiedStatement;
    }


    public int getOriginalStart()
    {
        return this.originalStart;
    }


    public int getOriginalCount()
    {
        return this.originalCount;
    }
}
