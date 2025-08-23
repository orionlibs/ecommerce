package de.hybris.platform.jalo.flexiblesearch.limit.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.jalo.flexiblesearch.limit.LimitStatementBuilder;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery;
import java.util.LinkedList;
import java.util.List;

public class HanaLimitStatementBuilder implements LimitStatementBuilder
{
    private final String modifiedStatement;
    private final List<Object> modifiedStatementValues;
    private final int originalStart;
    private final int originalCount;
    private final TranslatedQuery.ExecutableQuery originalQuery;
    private final boolean writeParametersAsLiteral;


    public HanaLimitStatementBuilder(TranslatedQuery.ExecutableQuery originalQuery, int originalStart, int originalCount, boolean parametersAsLiteral)
    {
        Preconditions.checkArgument((originalStart >= 0), "start parameter cannot be lower than 0");
        this.originalQuery = originalQuery;
        this.originalStart = originalStart;
        this.originalCount = originalCount;
        this.writeParametersAsLiteral = parametersAsLiteral;
        this.modifiedStatement = modifyOriginalStatement();
        this.modifiedStatementValues = modifyOriginalStatementValues();
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


    private String modifyOriginalStatement()
    {
        if(this.writeParametersAsLiteral)
        {
            int limitCount = (this.originalCount < 0) ? Integer.MAX_VALUE : this.originalCount;
            return this.originalQuery.getSQL() + " LIMIT " + this.originalQuery.getSQL() + " OFFSET " + limitCount;
        }
        return this.originalQuery.getSQL() + " LIMIT ? OFFSET ?";
    }


    private List<Object> modifyOriginalStatementValues()
    {
        if(this.writeParametersAsLiteral)
        {
            return new LinkedList(this.originalQuery.getValueList());
        }
        LinkedList<Object> result = new LinkedList(this.originalQuery.getValueList());
        int limitCount = (this.originalCount < 0) ? Integer.MAX_VALUE : this.originalCount;
        result.add(Integer.valueOf(limitCount));
        result.add(Integer.valueOf(this.originalStart));
        return result;
    }
}
