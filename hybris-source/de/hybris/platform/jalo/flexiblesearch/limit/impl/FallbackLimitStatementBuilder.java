package de.hybris.platform.jalo.flexiblesearch.limit.impl;

import de.hybris.platform.jalo.flexiblesearch.limit.LimitStatementBuilder;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery;
import java.util.List;

public class FallbackLimitStatementBuilder implements LimitStatementBuilder
{
    private final TranslatedQuery.ExecutableQuery originalQuery;
    private final int originalStart;
    private final int originalCount;


    public FallbackLimitStatementBuilder(TranslatedQuery.ExecutableQuery originalQuery, int originalStart, int originalCount)
    {
        this.originalQuery = originalQuery;
        this.originalStart = originalStart;
        this.originalCount = originalCount;
    }


    public boolean hasDbEngineLimitSupport()
    {
        return false;
    }


    public List<Object> getModifiedStatementValues()
    {
        return this.originalQuery.getValueList();
    }


    public String getModifiedStatement()
    {
        return this.originalQuery.getSQL();
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
