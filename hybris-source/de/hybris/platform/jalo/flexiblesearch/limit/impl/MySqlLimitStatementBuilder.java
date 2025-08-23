package de.hybris.platform.jalo.flexiblesearch.limit.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.jalo.flexiblesearch.limit.LimitStatementBuilder;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery;
import java.util.ArrayList;
import java.util.List;

public class MySqlLimitStatementBuilder implements LimitStatementBuilder
{
    private final List<Object> modifiedStatementValues;
    private final String modifiedStatement;
    private final int originalStart;
    private final int originalCount;


    public MySqlLimitStatementBuilder(TranslatedQuery.ExecutableQuery originalQuery, int originalStart, int originalCount)
    {
        Preconditions.checkArgument((originalStart >= 0), "start parameter cannot be lower than 0");
        this.originalStart = originalStart;
        this.originalCount = originalCount;
        this.modifiedStatement = originalQuery.getSQL() + " LIMIT ?,?";
        this.modifiedStatementValues = buildStatementValues(originalQuery.getValueList());
    }


    private List<Object> buildStatementValues(List<Object> valueList)
    {
        List<Object> result = new ArrayList(valueList.size() + 2);
        result.addAll(valueList);
        int limitCount = (this.originalCount < 0) ? Integer.MAX_VALUE : this.originalCount;
        result.add(Integer.valueOf(this.originalStart));
        result.add(Integer.valueOf(limitCount));
        return result;
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
