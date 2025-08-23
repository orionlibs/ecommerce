package de.hybris.platform.jalo.flexiblesearch.limit.impl;

import de.hybris.platform.jalo.flexiblesearch.limit.LimitStatementBuilder;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery;
import java.util.List;

public class HsqlLimitStatementBuilder implements LimitStatementBuilder
{
    private final LimitStatementBuilder state;


    public HsqlLimitStatementBuilder(TranslatedQuery.ExecutableQuery originalQuery, int originalStart, int originalCount)
    {
        if(originalCount == 0)
        {
            this.state = (LimitStatementBuilder)new FallbackLimitStatementBuilder(originalQuery, originalStart, originalCount);
        }
        else
        {
            this.state = (LimitStatementBuilder)new WorkingState(originalQuery, originalStart, originalCount);
        }
    }


    public boolean hasDbEngineLimitSupport()
    {
        return this.state.hasDbEngineLimitSupport();
    }


    public List<Object> getModifiedStatementValues()
    {
        return this.state.getModifiedStatementValues();
    }


    public String getModifiedStatement()
    {
        return this.state.getModifiedStatement();
    }


    public int getOriginalStart()
    {
        return this.state.getOriginalStart();
    }


    public int getOriginalCount()
    {
        return this.state.getOriginalCount();
    }
}
