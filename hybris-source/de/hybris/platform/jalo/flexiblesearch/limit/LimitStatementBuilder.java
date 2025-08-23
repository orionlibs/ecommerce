package de.hybris.platform.jalo.flexiblesearch.limit;

import java.util.List;

public interface LimitStatementBuilder
{
    boolean hasDbEngineLimitSupport();


    List<Object> getModifiedStatementValues();


    String getModifiedStatement();


    int getOriginalStart();


    int getOriginalCount();
}
